package es.serversurvival.socketWeb.messagges;

import es.serversurvival.socketWeb.SocketMessagge;

public abstract class SocketMessaggeExecutor {
    protected final String NO_RETURN_MESSAGGE = "";

    public abstract String getName();
    public abstract String execute(SocketMessagge messagge);
}
