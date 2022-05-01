package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.pagardividendos.EmpresaServerDividendoPagadoEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnEmpresaServerDividendoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServerDividendoPagadoEvento e) {
        Jugador jugador = jugadoresMySQL.getJugador(e.getJugador());
        double dividendo = e.getPixelcoins();
        
        jugadoresMySQL.setEstadisticas(e.getJugador(), jugador.getPixelcoins() + dividendo, jugador.getNVentas(),
                jugador.getIngresos() + dividendo, jugador.getGastos());
    }
}
