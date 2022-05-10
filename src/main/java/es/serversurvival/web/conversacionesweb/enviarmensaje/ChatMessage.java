package es.serversurvival.web.conversacionesweb.enviarmensaje;

import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
import es.serversurvival.web.webconnection.ServerSocketWeb;
import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import es.serversurvival.web.webconnection.socketmessages.SocketMessaggeExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatMessage extends SocketMessaggeExecutor {
    private final String name = "chat";
    private final ServerSocketWeb socket = ServerSocketWeb.INSTANCE;

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        String to = messagge.get("to");
        String message = messagge.get("message");
        String sender = messagge.get("sender");
        Player toPlayer = Bukkit.getPlayer(to);

        boolean hayConversacinEntreSenderYTo = conversacionesWebService.hayConversacionEntre(sender, to);

        if(!hayConversacinEntreSenderYTo){
            conversacionesWebService.findByServerNombre(to).ifPresent(this::cerrarChat);

            conversacionesWebService.save(new ConversacionWeb(sender, to));

            toPlayer.sendMessage(ChatColor.DARK_AQUA + "Te han enviado un mensaje desde la web. Para responder:  /re <mensaje>");
        }

        toPlayer.sendMessage(ChatColor.BOLD + "" + ChatColor.BOLD + sender + "> " + ChatColor.RESET + "" + ChatColor.YELLOW + message);
        toPlayer.playSound(toPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);

        return NO_RESPONSE;
    }

    private void cerrarChat(ConversacionWeb conversacion){
        conversacionesWebService.deleteByServerNombre(conversacion.getServerNombre());

        String message = "chatdisconnect-to=" + conversacion.getWebNombre() + "&sender=" + conversacion.getServerNombre();
        socket.enviarMensaje(new SocketMessagge(message));
    }

    @Override
    public String getName() {
        return name;
    }
}
