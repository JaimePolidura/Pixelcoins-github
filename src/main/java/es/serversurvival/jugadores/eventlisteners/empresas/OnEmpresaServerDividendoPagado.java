package es.serversurvival.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.pagardividendos.EmpresaServerDividendoPagadoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnEmpresaServerDividendoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServerDividendoPagadoEvento e) {
        Jugador jugador = jugadoresMySQL.getJugador(e.getJugador());
        double dividendo = e.getPixelcoins();
        
        jugadoresMySQL.setEstadisticas(e.getJugador(), jugador.getPixelcoins() + dividendo, jugador.getNventas(),
                jugador.getIngresos() + dividendo, jugador.getGastos());
    }
}
