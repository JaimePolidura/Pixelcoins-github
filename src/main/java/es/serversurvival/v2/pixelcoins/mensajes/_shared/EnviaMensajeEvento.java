package es.serversurvival.v2.pixelcoins.mensajes._shared;

import java.util.UUID;

public interface EnviaMensajeEvento {
    int tipoMensajeId();
    UUID destinatario();
    String[] formatMensajeValues();
}
