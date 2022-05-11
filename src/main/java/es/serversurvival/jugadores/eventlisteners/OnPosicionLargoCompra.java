package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionLargoCompra implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public OnPosicionLargoCompra() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void onPosicionLargoCompra(PosicionCompraLargoEvento evento) {
        Jugador jugador = jugadoresService.getByNombre(evento.getComprador());
        double precioTotal = evento.getPrecioTotal();

        this.jugadoresService.save(jugador.decrementPixelcoinsBy(precioTotal));
    }
}
