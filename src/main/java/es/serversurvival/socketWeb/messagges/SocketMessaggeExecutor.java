package es.serversurvival.socketWeb.messagges;

import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.socketWeb.SocketMessagge;

public abstract class SocketMessaggeExecutor {
    protected final ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
    protected final SocketMessagge NO_RESPONSE = null;

    public abstract String getName();
    public abstract SocketMessagge execute(SocketMessagge messagge);
}
