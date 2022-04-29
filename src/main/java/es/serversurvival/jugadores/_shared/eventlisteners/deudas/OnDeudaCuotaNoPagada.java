package es.serversurvival.jugadores._shared.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaNoPagadaEvento;

public final class OnDeudaCuotaNoPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        Jugador deudor = jugadoresMySQL.getJugador(evento.getDeudor());

        jugadoresMySQL.setNinpagos(evento.getDeudor(), deudor.getNInpagosDeuda() + 1);
    }
}
