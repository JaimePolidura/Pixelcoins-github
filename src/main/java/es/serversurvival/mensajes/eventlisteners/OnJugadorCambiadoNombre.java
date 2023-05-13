package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final MensajesService mensajesService;
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.mensajesService.findMensajesByDestinatario(evento.getAntiguoNombre()).stream()
                .map(mensaje -> mensaje.withDestinatario(evento.getNuevoNombre()))
                .forEach(this.mensajesService::save);
    }
}
