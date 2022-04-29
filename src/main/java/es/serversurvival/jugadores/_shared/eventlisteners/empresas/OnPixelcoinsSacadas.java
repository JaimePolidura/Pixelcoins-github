package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPixelcoinsSacadas implements AllMySQLTablesInstances {
    @EventListener
    public void onPixelcoinsSacadas (PixelcoinsSacadasEvento evento) {
        Jugador jugadorQueSaca = evento.getJugador();

        jugadoresMySQL.setEstadisticas(jugadorQueSaca.getNombre(), jugadorQueSaca.getPixelcoins() + evento.getPixelcoins(), jugadorQueSaca.getNVentas(),
                jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
    }
}
