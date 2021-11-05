package es.serversurvival.jugadores._shared.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaPagadaEvento;

public final class OnDeudaCuotaPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaPagada (DeudaCuotaPagadaEvento e) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(e.getDeudor(), e.getAcredor(), e.getPixelcoinsPagadas());

        Jugador deudor = jugadoresMySQL.getJugador(e.getDeudor());

        jugadoresMySQL.setNpagos(e.getDeudor(), deudor.getNpagos() + 1);
    }
}
