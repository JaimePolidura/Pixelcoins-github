package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;

public final class OnPixelcoinsSacadas implements AllMySQLTablesInstances {
    @EventListener
    public void onPixelcoinsSacadas (PixelcoinsSacadasEvento evento) {
        Jugador jugadorQueSaca = evento.getJugador();

        jugadoresMySQL.setEstadisticas(jugadorQueSaca.getNombre(), jugadorQueSaca.getPixelcoins() + evento.getPixelcoins(), jugadorQueSaca.getNventas(),
                jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
    }
}
