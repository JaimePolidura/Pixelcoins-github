package es.serversurvival.jugadores.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.tasks.DeudaCuotaNoPagadaEvento;

public final class OnDeudaCuotaNoPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        Jugador deudor = jugadoresMySQL.getJugador(evento.getDeudor());

        jugadoresMySQL.setNinpagos(evento.getDeudor(), deudor.getNinpagos() + 1);
    }
}
