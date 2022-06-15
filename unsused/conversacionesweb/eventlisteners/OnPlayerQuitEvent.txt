package es.serversurvival.web.conversacionesweb.eventlisteners;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.conversacionesweb._shared.application.ConversacionesWebService;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
import es.serversurvival.web.webconnection.ServerSocketWeb;
import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class OnPlayerQuitEvent implements Listener {
    private final ConversacionesWebService conversacionesWebService;
    private final ServerSocketWeb socket;

    public OnPlayerQuitEvent(){
        this.conversacionesWebService = DependecyContainer.get(ConversacionesWebService.class);
        this.socket = ServerSocketWeb.INSTANCE;
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        this.conversacionesWebService.findByServerNombre(playerName).ifPresent(conversacion -> {
            var message = "chatdisconnect-to=" + conversacion.getWebNombre() + "&sender=" + conversacion.getServerNombre();

            this.socket.enviarMensaje(new SocketMessagge(message));
        });
    }
}
