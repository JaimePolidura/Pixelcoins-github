package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.empresas.depositar.PixelcoinsDepositadasEvento;

public final class OnPixelcoinsDepositadas implements AllMySQLTablesInstances {
    @EventListener
    public void onPixelcoinsDepositadas (PixelcoinsDepositadasEvento evento) {
        Jugador jugador = evento.getJugador();

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - evento.getPixelcoins(),
                jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());
    }
}
