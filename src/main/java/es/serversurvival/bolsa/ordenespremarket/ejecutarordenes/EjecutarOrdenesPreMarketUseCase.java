package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.reducirPorcentaje;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo.ACCIONES;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;

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
        ActivoInfo activoInfo  = activoInfoService.getByNombreActivo(ticker, ACCIONES);
        double pixelcoinsJugador = jugadoresService.getByNombre(jugador).getPixelcoins();

        if(cantidad * activoInfo.getPrecio() >= pixelcoinsJugador) {
            this.eventBus.publish(new OrdenNoEjecutadoEvento(jugador, orden.getNombreActivo(), orden.getCantidad()));
            return;
        }

        comprarLargoUseCase.comprarLargo(jugador, ACCIONES, ticker, cantidad);
    }

    private void ejecutarOrdenCompraCorto (OrdenPremarket orden) {
        var posicionAbierta = this.posicionesAbiertasSerivce.getById(orden.getPosicionAbiertaId());
        if(posicionAbierta.getCantidad() < orden.getCantidad())
            this.eventBus.publish(new OrdenNoEjecutadoEvento(orden.getJugador(), orden.getNombreActivo(), orden.getCantidad()));

        comprarCortoUseCase.comprarPosicionCorto(orden.getPosicionAbiertaId(), orden.getCantidad(), orden.getJugador());
    }

    private void ejecutarOrdenVentaCorto (OrdenPremarket orden) {
        ActivoInfo activoInfo = activoInfoService.getByNombreActivo(orden.getNombreActivo(), ACCIONES);
        double precio = activoInfo.getPrecio();

        Jugador jugador = jugadoresService.getByNombre(orden.getJugador());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precio * orden.getCantidad();
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

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
