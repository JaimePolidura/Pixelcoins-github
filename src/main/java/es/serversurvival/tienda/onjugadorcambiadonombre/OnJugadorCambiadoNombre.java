package es.serversurvival.tienda.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import lombok.AllArgsConstructor;

import java.util.List;

@EventHandler
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final TiendaService tiendaService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        List<TiendaObjeto> objetosTiendaJugador = this.tiendaService.findByJugador(evento.getAntiguoNombre());

        for (TiendaObjeto tiendaObjeto : objetosTiendaJugador) {
            this.tiendaService.save(tiendaObjeto.withJugador(evento.getNuevoNombre()));
        }
    }
}
