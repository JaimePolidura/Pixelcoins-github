package es.serversurvival.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPixelcoinsSacadas implements AllMySQLTablesInstances {
    @EventListener
    public void onPixelcoinsSacadas (PixelcoinsSacadasEvento evento) {
        Jugador jugadorQueSaca = evento.getJugador();

        jugadoresMySQL.setEstadisticas(jugadorQueSaca.getNombre(), jugadorQueSaca.getPixelcoins() + evento.getPixelcoins(), jugadorQueSaca.getNventas(),
                jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
    }
}
