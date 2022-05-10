package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.vendercorto.PosicionVentaCortoEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCortoVenta implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public OnPosicionCortoVenta() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void onPosicionCortoVenta (PosicionVentaCortoEvento evento){
        Jugador jugador = jugadoresService.getByNombre(evento.getComprador());

        jugadoresService.save(jugador.decrementPixelcoinsBy(evento.getPrecioTotal()).incrementGastosBy(evento.getPrecioTotal()));
    }
}
