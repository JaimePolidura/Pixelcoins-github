package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.pagarsueldostask.SueldoPagadoEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnSueldoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void onSueldoPagado (SueldoPagadoEvento e) {
        Jugador empleadoAPagar = jugadoresMySQL.getJugador(e.getEmpleado());

        System.out.println(e.getEmpleado());

        jugadoresMySQL.setEstadisticas(e.getEmpleado(), empleadoAPagar.getPixelcoins() + e.getSueldo(), empleadoAPagar.getNVentas(),
                empleadoAPagar.getIngresos() + e.getSueldo(), empleadoAPagar.getGastos());
    }
}
