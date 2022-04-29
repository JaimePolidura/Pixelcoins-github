package es.serversurvival.jugadores._shared.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.comprarcorto.PosicionCompraCortoEvento;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCortoCompra implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionCortoCompra (PosicionCompraCortoEvento evento) {
        Jugador jugador = jugadoresMySQL.getJugador(evento.getVendedor());

        double revalorizacionTotal = (evento.getPrecioApertura() - evento.getPrecioCierre()) * evento.getCantidad();

        double pixelcoinsJugador = jugador.getPixelcoins();
        if(0 > pixelcoinsJugador + revalorizacionTotal)
            jugadoresMySQL.setEstadisticas(jugador.getNombre(), pixelcoinsJugador + revalorizacionTotal, jugador.getNVentas(),
                    jugador.getIngresos(), jugador.getGastos() + revalorizacionTotal);
        else
            jugadoresMySQL.setEstadisticas(jugador.getNombre(), pixelcoinsJugador + revalorizacionTotal, jugador.getNVentas(),
                    jugador.getIngresos() + revalorizacionTotal, jugador.getGastos());

    }
}
