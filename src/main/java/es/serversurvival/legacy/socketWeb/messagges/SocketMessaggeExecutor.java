package es.serversurvival.legacy.socketWeb.messagges;

import es.serversurvival.legacy.mySQL.ConversacionesWeb;
import es.serversurvival.legacy.socketWeb.SocketMessagge;

public abstract class SocketMessaggeExecutor {
    protected final ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
    protected final SocketMessagge NO_RESPONSE = null;

    public abstract String getName();
    public abstract SocketMessagge execute(SocketMessagge messagge);
}
