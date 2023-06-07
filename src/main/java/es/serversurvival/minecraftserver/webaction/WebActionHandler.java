package es.serversurvival.minecraftserver.webaction;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;

import java.util.UUID;

public interface WebActionHandler<T extends WebActionRequestBody> {
    void handle(UUID jugadorId, T body) throws WebActionException;
}
