package es.serversurvival.bolsa._shared.ordenespremarket.ejecutarordenes;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.bolsa._shared.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.bolsa._shared.ordenespremarket.mysql.OrdenPreMarket;
import es.serversurvival.bolsa.vendercorto.VenderCortoUseCase;
import es.serversurvival.bolsa.venderlargo.VenderLargoUseCase;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import main.Pair;

import java.util.List;

import static es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo.*;
import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.reducirPorcentaje;

public final class EjecutarOrdenesPreMarketUseCase implements AllMySQLTablesInstances {
    public static final EjecutarOrdenesPreMarketUseCase INSTANCE = new EjecutarOrdenesPreMarketUseCase();

    private final ComprarLargoUseCase comprarLargoUseCase = ComprarLargoUseCase.INSTANCE;
    private final VenderLargoUseCase venderLargoUseCase = VenderLargoUseCase.INSTANCE;
    private final VenderCortoUseCase venderCortoUseCase = VenderCortoUseCase.INSTANCE;
    private final ComprarCortoUseCase comprarCortoUseCase = ComprarCortoUseCase.INSTANCE;

    private EjecutarOrdenesPreMarketUseCase() {}

    public void ejecutarOrdenes () {
        List<OrdenPreMarket> todasLasOrdenes = ordenesMySQL.getAllOrdenes();

        POOL.submit(() -> {
            todasLasOrdenes.forEach(orden -> {
                if(orden.getAccion_orden() == AccionOrden.LARGO_COMPRA){
                    ejecutarOrdenCompraLargo(orden);

                }else if (orden.getAccion_orden() == AccionOrden.LARGO_VENTA){
                    ejecutarOrdenVentaLargo(orden);

                }else if (orden.getAccion_orden() == AccionOrden.CORTO_VENTA) {
                    ejecutarOrdenVentaCorto(orden);

                }else if (orden.getAccion_orden() == AccionOrden.CORTO_COMPRA){
                    ejecutarOrdenCompraCorto(orden);
                }

                ordenesMySQL.borrarOrden(orden.getId());
            });
        });
    }

    private void ejecutarOrdenVentaLargo(OrdenPreMarket orden) {
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        int id = Integer.parseInt(orden.getNombre_activo());
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(id);

        venderLargoUseCase.venderPosicion(posicionAbierta, cantidad, jugador);
    }

    private void ejecutarOrdenCompraLargo(OrdenPreMarket orden) {
        String ticker = orden.getNombre_activo();
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();

        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker).get();

        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();
        double pixelcoinsJugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();

        if(cantidad * precio >= pixelcoinsJugador)
            cantidad = (int) (pixelcoinsJugador / precio);

        if(cantidad == 0) {
            Pixelcoin.publish(new OrdenNoEjecutadoEvento(jugador, orden));
            return;
        }

        comprarLargoUseCase.abrir(ACCIONES, ticker, nombreValor, "acciones", precio, cantidad, jugador);
    }

    private void ejecutarOrdenCompraCorto (OrdenPreMarket orden) {
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(orden.getNombre_activo()));

        comprarCortoUseCase.comprarPosicionCorto(posicionAbierta, orden.getCantidad(), orden.getJugador());
    }

    private void ejecutarOrdenVentaCorto (OrdenPreMarket orden) {
        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(orden.getNombre_activo()).get();
        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();

        Jugador jugador = jugadoresMySQL.getJugador(orden.getJugador());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = pairNombrePrecio.getValue() * orden.getCantidad();
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

        if(comision > dineroJugador){
            Pixelcoin.publish(new OrdenNoEjecutadoEvento(jugador.getNombre(), orden));
            return;
        }

        venderCortoUseCase.venderEnCortoBolsa(jugador.getNombre(), orden.getNombre_activo(), nombreValor, orden.getCantidad(), precio);
    }
}
