package es.serversurvival.v2.pixelcoins.mensajes;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v2.pixelcoins.mensajes._shared.EnviaMensajeEvento;
import es.serversurvival.v2.pixelcoins.mensajes._shared.EnviadorMensajes;
import es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes.TipoMensaje;
import es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes.TipoMensajeService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnEventoEnviaMensaje {
    private final TipoMensajeService tipoMensajeService;
    private final EnviadorMensajes enviadorMensajes;

    @EventListener({EnviaMensajeEvento.class})
    public void on(EnviaMensajeEvento enviaMensajeEvento) {
        TipoMensaje tipoMensaje = tipoMensajeService.getById(enviaMensajeEvento.tipoMensajeId());
        String mensajeFormateado = String.format(tipoMensaje.getMensajeSinFormatear(), enviaMensajeEvento.formatMensajeValues());

        enviadorMensajes.enviar(
                enviaMensajeEvento.destinatario(), tipoMensaje.getTipoContenido(), mensajeFormateado, enviaMensajeEvento.tipoMensajeId()
        );
    }
}
