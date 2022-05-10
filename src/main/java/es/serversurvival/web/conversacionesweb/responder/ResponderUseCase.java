package es.serversurvival.web.conversacionesweb.responder;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.conversacionesweb._shared.application.ConversacionesWebService;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
import es.serversurvival.web.webconnection.ServerSocketWeb;
import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;

public final class ResponderUseCase {
    private final ConversacionesWebService conversacionesWebService;
    private final ServerSocketWeb socket;

    public ResponderUseCase(){
        this.conversacionesWebService = DependecyContainer.get(ConversacionesWebService.class);
        this.socket = ServerSocketWeb.INSTANCE;
    }

    public void responder(String sender, String mensaje){
        var conversacion = this.ensureConversacioneExists(sender);

        String mensajeSocket = "chat-sender=" + sender + "&to=" + conversacion.getWebNombre() + "&message=" + mensaje;
        socket.enviarMensaje(new SocketMessagge(mensajeSocket));
    }

    private ConversacionWeb ensureConversacioneExists(String sender){
        return this.conversacionesWebService.findByServerNombre(sender)
                .orElseThrow(() -> new ResourceNotFound("No tienes ninguna conversacion abierta"));
    }
}
