package es.serversurvival.bolsa.other.comprarlargo;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class ComprarLargoUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public ComprarLargoUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void comprar(TipoActivo tipoActivo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String jugadorNombre) {
        Jugador jugador = jugadoresService.getByNombre(jugadorNombre);
        double totalPrice = precioUnidad * cantidad;
        posicionesAbiertasSerivce.save(jugadorNombre, tipoActivo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);

        this.jugadoresService.save(jugador.decrementPixelcoinsBy(totalPrice));

        Pixelcoin.publish(new PosicionCompraLargoEvento(jugadorNombre, precioUnidad, cantidad, cantidad*precioUnidad, ticker, tipoActivo, nombreValor, alias));
    }
}
