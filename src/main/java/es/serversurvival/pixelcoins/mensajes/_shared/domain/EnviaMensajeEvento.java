package es.serversurvival.pixelcoins.mensajes._shared.domain;

import java.util.UUID;

public interface EnviaMensajeEvento {
    String mensaje();
    UUID destinatario();
    TipoMensaje tipoMensaje();
}
