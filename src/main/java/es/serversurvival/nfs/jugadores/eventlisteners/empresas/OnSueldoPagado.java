package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.empresas.tasks.SueldoPagadoEvento;

public final class OnSueldoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void onSueldoPagado (SueldoPagadoEvento e) {
        Jugador empleadoAPagar = jugadoresMySQL.getJugador(e.getEmpleado());

        jugadoresMySQL.setEstadisticas(e.getEmpleado(), empleadoAPagar.getPixelcoins() + e.getSueldo(), empleadoAPagar.getNventas(),
                empleadoAPagar.getIngresos() + e.getSueldo(), empleadoAPagar.getGastos());
    }
}
