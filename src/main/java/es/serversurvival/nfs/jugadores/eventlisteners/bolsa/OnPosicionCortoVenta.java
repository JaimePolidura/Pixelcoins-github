package es.serversurvival.nfs.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionVentaCortoEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;

public final class OnPosicionCortoVenta implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionCortoVenta (PosicionVentaCortoEvento evento){
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - evento.getPrecioTotal(), jugador.getNventas(),
                jugador.getIngresos(), jugador.getGastos() + evento.getPrecioTotal());
    }
}
