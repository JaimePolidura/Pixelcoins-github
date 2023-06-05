package es.serversurvival.v2.minecraftserver.webaction;

import es.serversurvival.v2.minecraftserver.webaction.messages.WebActionRequestBody;

import java.util.UUID;

public interface WebActionHandler<T extends WebActionRequestBody> {
    void handle(UUID jugadorId, T body) throws WebActionException;
}
