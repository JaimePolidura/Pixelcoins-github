package es.serversurvival.jugadores.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.tasks.DeudaCuotaPagadaEvento;

public final class OnDeudaCuotaPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaPagada (DeudaCuotaPagadaEvento evento) {
        Jugador deudor = jugadoresMySQL.getJugador(evento.getDeudor());

        jugadoresMySQL.setNpagos(evento.getDeudor(), deudor.getNpagos() + 1);
    }
}
