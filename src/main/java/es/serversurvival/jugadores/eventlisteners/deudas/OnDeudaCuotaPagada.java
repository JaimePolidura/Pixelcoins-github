package es.serversurvival.jugadores.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.tasks.DeudaCuotaPagadaEvento;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;

public final class OnDeudaCuotaPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onCuotaPagada (DeudaCuotaPagadaEvento e) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(e.getDeudor(), e.getAcredor(), e.getPixelcoinsPagadas());

        Jugador deudor = jugadoresMySQL.getJugador(e.getDeudor());

        jugadoresMySQL.setNpagos(e.getDeudor(), deudor.getNpagos() + 1);
    }
}
