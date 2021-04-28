package es.serversurvival.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.PosicionVentaCortoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCortoVenta implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionCortoVenta (PosicionVentaCortoEvento evento){
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - evento.getPrecioTotal(), jugador.getNventas(),
                jugador.getIngresos(), jugador.getGastos() + evento.getPrecioTotal());
    }
}
