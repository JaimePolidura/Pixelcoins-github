package es.serversurvival.v2.pixelcoins.mensajes;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v2.pixelcoins.mensajes._shared.EnviaMensajeEvento;
import es.serversurvival.v2.pixelcoins.mensajes._shared.EnviadorMensajes;
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
