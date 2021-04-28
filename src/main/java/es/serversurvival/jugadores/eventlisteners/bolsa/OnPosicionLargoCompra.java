package es.serversurvival.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionLargoCompra implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionLargoCompra(PosicionCompraLargoEvento evento) {
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());
        double precioTotal = evento.getPrecioTotal();

        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - precioTotal);
    }
}
