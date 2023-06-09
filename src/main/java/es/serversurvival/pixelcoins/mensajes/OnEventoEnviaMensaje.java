package es.serversurvival.pixelcoins.mensajes;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.mensajes._shared.domain.EnviaMensajeEvento;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnEventoEnviaMensaje {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener({EnviaMensajeEvento.class})
    public void on(EnviaMensajeEvento evento) {
        enviadorMensajes.enviar(
                evento.tipoMensaje(), evento.destinatario(), evento.mensaje()
        );
    }
}
