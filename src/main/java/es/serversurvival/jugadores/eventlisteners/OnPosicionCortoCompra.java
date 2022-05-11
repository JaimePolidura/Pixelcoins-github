package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other.comprarcorto.PosicionCompraCortoEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCortoCompra implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public OnPosicionCortoCompra() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void onPosicionCortoCompra (PosicionCompraCortoEvento evento) {
        Jugador jugador = jugadoresService.getByNombre(evento.getVendedor());
        double revalorizacionTotal = (evento.getPrecioApertura() - evento.getPrecioCierre()) * evento.getCantidad();
        double pixelcoinsJugador = jugador.getPixelcoins();

        if(0 > pixelcoinsJugador + revalorizacionTotal)
            jugadoresService.save(jugador.incrementPixelcoinsBy(revalorizacionTotal).incrementGastosBy(revalorizacionTotal));
        else
            jugadoresService.save(jugador.incrementPixelcoinsBy(revalorizacionTotal).incrementIngresosBy(revalorizacionTotal));

    }
}
