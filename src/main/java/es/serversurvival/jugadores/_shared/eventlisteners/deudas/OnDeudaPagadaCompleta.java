package es.serversurvival.jugadores._shared.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.deudas.pagarTodo.DeudaPagadaCompletaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnDeudaPagadaCompleta implements AllMySQLTablesInstances {
    @EventListener
    public void on (DeudaPagadaCompletaEvento e) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(e.getDeudor(), e.getAcredor(), e.getPixelcoins());
    }
}
