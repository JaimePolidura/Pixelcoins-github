package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas.old.pagardividendos.DividendoPagadoEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class OnDividendoPagado implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public OnDividendoPagado() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void on (DividendoPagadoEvento evento) {
        Jugador jugador = this.jugadoresService.getByNombre(evento.getJugador());

        this.jugadoresService.save(jugador.incrementPixelcoinsBy(evento.getPixelcoins()));
    }
}
