package es.serversurvival.bolsa.other._shared.ordenespremarket.ejecutarordenes;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.other.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.other.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.other.vendercorto.VenderCortoUseCase;
import es.serversurvival.bolsa.other.venderlargo.VenderLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import main.Pair;

import java.util.List;

import static es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo.*;
import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.reducirPorcentaje;

public final class EjecutarOrdenesPreMarketUseCase implements AllMySQLTablesInstances {
    public static final EjecutarOrdenesPreMarketUseCase INSTANCE = new EjecutarOrdenesPreMarketUseCase();
    private final JugadoresService jugadoresService;

    private final ComprarLargoUseCase comprarLargoUseCase = ComprarLargoUseCase.INSTANCE;
    private final VenderLargoUseCase venderLargoUseCase = VenderLargoUseCase.INSTANCE;
    private final VenderCortoUseCase venderCortoUseCase = VenderCortoUseCase.INSTANCE;
    private final ComprarCortoUseCase comprarCortoUseCase = ComprarCortoUseCase.INSTANCE;

    private EjecutarOrdenesPreMarketUseCase() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void ejecutarOrdenes () {
        List<OrdenPremarket> todasLasOrdenes = ordenesMySQL.getAllOrdenes();

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

                ordenesMySQL.borrarOrden(orden.getOrderPremarketId());
            });
        });
    }

    private void ejecutarOrdenVentaLargo(OrdenPremarket orden) {
        int cantidad = orden.getCantidad();
        String jugador = orden.getJugador();
        int id = Integer.parseInt(orden.getNombreActivo());
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(id);

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

        comprarLargoUseCase.abrir(ACCIONES, ticker, nombreValor, "acciones", precio, cantidad, jugador);
    }

    private void ejecutarOrdenCompraCorto (OrdenPremarket orden) {
        PosicionAbierta posicionAbierta = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(orden.getNombreActivo()));

        comprarCortoUseCase.comprarPosicionCorto(posicionAbierta, orden.getCantidad(), orden.getJugador());
    }

    private void ejecutarOrdenVentaCorto (OrdenPremarket orden) {
        Pair<String, Double> pairNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(orden.getNombreActivo()).get();
        double precio = pairNombrePrecio.getValue();
        String nombreValor = pairNombrePrecio.getKey();

        Jugador jugador = jugadoresService.getByNombre(orden.getJugador());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = pairNombrePrecio.getValue() * orden.getCantidad();
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

        if(comision > dineroJugador){
            Pixelcoin.publish(new OrdenNoEjecutadoEvento(jugador.getNombre(), orden));
            return;
        }

        venderCortoUseCase.venderEnCortoBolsa(jugador.getNombre(), orden.getNombreActivo(), nombreValor, orden.getCantidad(), precio);
    }
}
