package es.serversurvival.nfs.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCompraLargoEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;

public final class OnPosicionLargoCompra implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionLargoCompra(PosicionCompraLargoEvento evento) {
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());
        double precioTotal = evento.getPrecioTotal();

        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - precioTotal);
    }
}
