package es.serversurvival.tienda.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;

import java.util.List;

public final class OnJugadorCambiadoNombre {
    private final TiendaService tiendaService;

    public OnJugadorCambiadoNombre(){
        this.tiendaService = DependecyContainer.get(TiendaService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        List<TiendaObjeto> objetosTiendaJugador = this.tiendaService.findByJugador(evento.getAntiguoNombre());

        for (TiendaObjeto tiendaObjeto : objetosTiendaJugador) {
            this.tiendaService.save(tiendaObjeto.withJugador(evento.getNuevoNombre()));
        }
    }
}
