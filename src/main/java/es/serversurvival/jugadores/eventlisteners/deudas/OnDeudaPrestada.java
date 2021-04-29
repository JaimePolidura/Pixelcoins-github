package es.serversurvival.jugadores.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.deudas.prestar.PixelcoinsPrestadasEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;

public final class OnDeudaPrestada implements AllMySQLTablesInstances {
    @EventListener
    public void on (PixelcoinsPrestadasEvento e) {
        jugadoresMySQL.realizarTransferencia(e.getAcredor(), e.getDeudor(), Funciones.aumentarPorcentaje(e.getPixelcoins(), e.getIntereses()));
    }
}
