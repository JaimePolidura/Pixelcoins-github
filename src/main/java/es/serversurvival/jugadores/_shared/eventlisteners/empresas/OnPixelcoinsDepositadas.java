package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.depositar.PixelcoinsDepositadasEvento;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPixelcoinsDepositadas implements AllMySQLTablesInstances {
    @EventListener
    public void onPixelcoinsDepositadas (PixelcoinsDepositadasEvento evento) {
        Jugador jugador = evento.getJugador();

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - evento.getPixelcoins(),
                jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());
    }
}
