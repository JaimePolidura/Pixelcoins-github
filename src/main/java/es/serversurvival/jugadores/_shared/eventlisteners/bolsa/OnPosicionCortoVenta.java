package es.serversurvival.jugadores._shared.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.vendercorto.PosicionVentaCortoEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCortoVenta implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionCortoVenta (PosicionVentaCortoEvento evento){
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - evento.getPrecioTotal(), jugador.getNVentas(),
                jugador.getIngresos(), jugador.getGastos() + evento.getPrecioTotal());
    }
}
