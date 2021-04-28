package es.serversurvival.webconnection.socketmessages;

import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionesWeb;

public abstract class SocketMessaggeExecutor {
    protected final ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
    protected final SocketMessagge NO_RESPONSE = null;

    public abstract String getName();
    public abstract SocketMessagge execute(SocketMessagge messagge);
}
