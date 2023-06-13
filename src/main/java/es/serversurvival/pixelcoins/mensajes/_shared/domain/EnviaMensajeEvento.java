package es.serversurvival.pixelcoins.mensajes._shared.domain;

import es.jaime.Event;

import java.util.UUID;

public interface EnviaMensajeEvento extends Event {
    String mensaje();
    UUID destinatario();
    TipoMensaje tipoMensaje();
}
