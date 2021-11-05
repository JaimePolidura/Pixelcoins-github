package es.serversurvival.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.jaime.TransactionalEventListener;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.transacciones.mySQL.Transaccion;

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
