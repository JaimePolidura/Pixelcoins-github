package es.serversurvival.nfs.jugadores.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.deudas.tasks.DeudaCuotaNoPagadaEvento;

public final class OnDeudaCuotaNoPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        Jugador deudor = jugadoresMySQL.getJugador(evento.getDeudor());

        jugadoresMySQL.setNinpagos(evento.getDeudor(), deudor.getNinpagos() + 1);
    }
}
