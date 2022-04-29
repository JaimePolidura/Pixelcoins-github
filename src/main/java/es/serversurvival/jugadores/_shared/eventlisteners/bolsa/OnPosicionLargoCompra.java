package es.serversurvival.jugadores._shared.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.jaime.TransactionalEventListener;
import es.serversurvival.bolsa.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class OnPosicionLargoCompra implements AllMySQLTablesInstances, TransactionalEventListener {
    private PosicionCompraLargoEvento ultimoEventoEjecutado;

    @EventListener
    public void onPosicionLargoCompra(PosicionCompraLargoEvento evento) {
        this.ultimoEventoEjecutado = evento;

        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());
        double precioTotal = evento.getPrecioTotal();

        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - precioTotal);
    }

    @Override
    public void rollback() {
        Jugador jugador = jugadoresMySQL.getJugador(ultimoEventoEjecutado.getComprador());
        double precioTotal = ultimoEventoEjecutado.getPrecioTotal();

        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() + precioTotal);
    }
}
