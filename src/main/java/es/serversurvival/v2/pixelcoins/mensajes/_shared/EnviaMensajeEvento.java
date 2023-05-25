package es.serversurvival.v2.pixelcoins.mensajes._shared;

import java.util.UUID;

public interface EnviaMensajeEvento {
    String mensaje();
    UUID destinatario();
    TipoMensaje tipoMensaje();
}
