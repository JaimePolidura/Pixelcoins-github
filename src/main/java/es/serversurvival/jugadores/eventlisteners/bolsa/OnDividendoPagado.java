package es.serversurvival.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa._shared.posicionesabiertas.pagardividendos.DividendoPagadoEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnDividendoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void on (DividendoPagadoEvento evento) {
        jugadoresMySQL.setPixelcoin(evento.getJugador(), jugadoresMySQL.getJugador(evento.getJugador()).getPixelcoins() + evento.getPixelcoins());
    }
}
