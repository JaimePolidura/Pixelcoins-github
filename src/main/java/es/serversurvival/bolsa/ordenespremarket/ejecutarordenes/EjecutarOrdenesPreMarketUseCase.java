package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
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
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import main.Pair;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.reducirPorcentaje;
import static es.serversurvival.bolsa.activosinfo._shared.domain.TipoActivo.ACCIONES;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;

public final class EjecutarOrdenesPreMarketUseCase implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;
    private final OrdenesPremarketService ordenesPremarketService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    private final ComprarLargoUseCase comprarLargoUseCase;
    private final VenderLargoUseCase venderLargoUseCase;
    private final VenderCortoUseCase venderCortoUseCase;
    private final ComprarCortoUseCase comprarCortoUseCase;

    public EjecutarOrdenesPreMarketUseCase() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.comprarLargoUseCase = new ComprarLargoUseCase();
        this.venderCortoUseCase = new VenderCortoUseCase();
        this.comprarCortoUseCase = new ComprarCortoUseCase();
        this.venderLargoUseCase = new VenderLargoUseCase();
    }

    public void ejecutarOrdenes () {
        List<OrdenPremarket> todasLasOrdenes = ordenesPremarketService.findAll();

        POOL.submit(() -> {
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
        });
    }

    private void ejecutarOrdenVentaLargo(OrdenPremarket orden) {
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        PosicionAbierta posicionAbierta = posicionesAbiertasSerivce.getById(orden.getPosicionAbiertaId());

        venderLargoUseCase.venderPosicion(posicionAbierta, cantidad, jugador);
    }

    private void ejecutarOrdenCompraLargo(OrdenPremarket orden) {
        String ticker = orden.getNombreActivo();
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker).get();

        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();
        double pixelcoinsJugador = jugadoresService.getByNombre(jugador).getPixelcoins();

        if(cantidad * precio >= pixelcoinsJugador)
            cantidad = (int) (pixelcoinsJugador / precio);

        if(cantidad == 0) {
            Pixelcoin.publish(new OrdenNoEjecutadoEvento(jugador, orden));
            return;
        }

        comprarLargoUseCase.comprarLargo(ACCIONES, ticker, cantidad, jugador);
    }

    private void ejecutarOrdenCompraCorto (OrdenPremarket orden) {
        comprarCortoUseCase.comprarPosicionCorto(orden.getPosicionAbiertaId(), orden.getCantidad(), orden.getJugador());
    }

    private void ejecutarOrdenVentaCorto (OrdenPremarket orden) {
        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(orden.getNombreActivo()).get();
        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();

        Jugador jugador = jugadoresService.getByNombre(orden.getJugador());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = pairNombrePrecio.getValue() * orden.getCantidad();
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

        if(comision > dineroJugador){
            Pixelcoin.publish(new OrdenNoEjecutadoEvento(jugador.getNombre(), orden));
            return;
        }

        venderCortoUseCase.venderEnCortoBolsa(jugador.getNombre(), orden.getNombreActivo(), nombreValor, orden.getCantidad(), precio);
    }
}
