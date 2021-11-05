package es.serversurvival.jugadores._shared.eventlisteners.deudas;

import es.jaime.EventListener;
import es.serversurvival.deudas.prestar.PixelcoinsPrestadasEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;

public final class OnDeudaPrestada implements AllMySQLTablesInstances {
    @EventListener
    public void on (PixelcoinsPrestadasEvento e) {
        jugadoresMySQL.realizarTransferencia(e.getAcredor(), e.getDeudor(), Funciones.aumentarPorcentaje(e.getPixelcoins(), e.getIntereses()));
    }
}
