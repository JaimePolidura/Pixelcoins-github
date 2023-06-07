package es.serversurvival.pixelcoins.mensajes._shared;

import java.util.UUID;

public interface EnviaMensajeEvento {
    String mensaje();
    UUID destinatario();
    TipoMensaje tipoMensaje();
}
