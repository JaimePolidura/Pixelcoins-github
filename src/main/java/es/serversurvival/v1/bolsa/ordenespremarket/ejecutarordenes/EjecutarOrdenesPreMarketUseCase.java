package es.serversurvival.v1.bolsa.ordenespremarket.ejecutarordenes;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.v1.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.v1.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@UseCase
@RequiredArgsConstructor
public final class EjecutarOrdenesPreMarketUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final OrdenesPremarketService ordenesPremarketService;
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ComprarCortoUseCase comprarCortoUseCase;
    private final ActivosInfoService activoInfoService;
    private final VenderLargoUseCase venderLargoUseCase;
    private final VenderCortoUseCase venderCortoUseCase;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    private AtomicBoolean isLoading = new AtomicBoolean(false);

    public void ejecutarOrdenes () {
        isLoading.set(true);

        List<OrdenPremarket> todasLasOrdenes = ordenesPremarketService.findAll();

        todasLasOrdenes.forEach(orden -> {
            if(orden.getTipoAccion() == TipoAccion.LARGO_COMPRA){
                ejecutarOrdenCompraLargo(orden);

            }else if (orden.getTipoAccion() == TipoAccion.LARGO_VENTA){
                ejecutarOrdenVentaLargo(orden);

            }else if (orden.getTipoAccion() == TipoAccion.CORTO_VENTA) {
                ejecutarOrdenVentaCorto(orden);

            }else if (orden.getTipoAccion() == TipoAccion.CORTO_COMPRA){
                ejecutarOrdenCompraCorto(orden);
            }

            ordenesPremarketService.deleteById(orden.getOrderPremarketId());
        });

        isLoading.set(false);
    }

    private void ejecutarOrdenVentaLargo(OrdenPremarket orden) {
        int orderCantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        PosicionAbierta posicionAbierta = posicionesAbiertasSerivce.getById(orden.getPosicionAbiertaId());

        if(orderCantidad > posicionAbierta.getCantidad()){
            this.eventBus.publish(new OrdenNoEjecutadoEvento(jugador, orden.getNombreActivo(), orden.getCantidad()));
            return;
        }

        venderLargoUseCase.venderPosicion(posicionAbierta.getPosicionAbiertaId(), orderCantidad, jugador);
    }

    private void ejecutarOrdenCompraLargo(OrdenPremarket orden) {
        String ticker = orden.getNombreActivo();
        int cantidad = orden.getCantidad();

        String jugador = orden.getJugador();
        ActivoInfo activoInfo  = activoInfoService.getByNombreActivo(ticker, TipoActivo.ACCIONES);
        double pixelcoinsJugador = jugadoresService.getByNombre(jugador).getPixelcoins();

        if(cantidad * activoInfo.getPrecio() >= pixelcoinsJugador) {
            this.eventBus.publish(new OrdenNoEjecutadoEvento(jugador, orden.getNombreActivo(), orden.getCantidad()));
            return;
        }

        comprarLargoUseCase.comprarLargo(jugador, TipoActivo.ACCIONES, ticker, cantidad);
    }

    private void ejecutarOrdenCompraCorto (OrdenPremarket orden) {
        var posicionAbierta = this.posicionesAbiertasSerivce.getById(orden.getPosicionAbiertaId());
        if(posicionAbierta.getCantidad() < orden.getCantidad())
            this.eventBus.publish(new OrdenNoEjecutadoEvento(orden.getJugador(), orden.getNombreActivo(), orden.getCantidad()));

        comprarCortoUseCase.comprarPosicionCorto(orden.getPosicionAbiertaId(), orden.getCantidad(), orden.getJugador());
    }

    private void ejecutarOrdenVentaCorto (OrdenPremarket orden) {
        ActivoInfo activoInfo = activoInfoService.getByNombreActivo(orden.getNombreActivo(), TipoActivo.ACCIONES);
        double precio = activoInfo.getPrecio();

        Jugador jugador = jugadoresService.getByNombre(orden.getJugador());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precio * orden.getCantidad();
        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PosicionesAbiertasSerivce.PORCENTAJE_CORTO), 2);

        if(comision > dineroJugador){
            this.eventBus.publish(new OrdenNoEjecutadoEvento(jugador.getNombre(), orden.getNombreActivo(), orden.getCantidad()));
            return;
        }

        venderCortoUseCase.venderEnCortoBolsa(jugador.getNombre(), orden.getNombreActivo(), orden.getCantidad());
    }

    public boolean isLoading(){
        return this.isLoading.get();
    }
}
