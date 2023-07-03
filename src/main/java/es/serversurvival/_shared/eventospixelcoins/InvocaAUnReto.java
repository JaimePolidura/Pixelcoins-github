package es.serversurvival._shared.eventospixelcoins;

import es.jaime.Event;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;

import java.util.Map;
import java.util.UUID;

public interface InvocaAUnReto extends Event {
    Map<UUID, RetoMapping> retosByJugadorId();

    default Object otroDatoReto() {
        return null;
    }
}
