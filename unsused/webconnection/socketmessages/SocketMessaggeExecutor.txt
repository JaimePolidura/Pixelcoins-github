package es.serversurvival.web.webconnection.socketmessages;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.conversacionesweb._shared.application.ConversacionesWebService;

public abstract class SocketMessaggeExecutor {
    protected final SocketMessagge NO_RESPONSE = null;
    protected final ConversacionesWebService conversacionesWebService;

    public SocketMessaggeExecutor(){
        this.conversacionesWebService = DependecyContainer.get(ConversacionesWebService.class);
    }

    public abstract String getName();
    public abstract SocketMessagge execute(SocketMessagge messagge);
}
