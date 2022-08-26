package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnJugadorCambiadoNombre {
    private final MensajesService mensajesService;

    public OnJugadorCambiadoNombre(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.mensajesService.findMensajesByDestinatario(evento.getAntiguoNombre()).stream()
                .map(mensaje -> mensaje.withDestinatario(evento.getNuevoNombre()))
                .forEach(this.mensajesService::save);
    }
}
