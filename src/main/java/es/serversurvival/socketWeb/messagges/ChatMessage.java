package es.serversurvival.socketWeb.messagges;

import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatMessage extends SocketMessaggeExecutor{
    private final String name = "chat";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        String to = messagge.get("to");
        String message = messagge.get("message");
        String sender = messagge.get("sender");
        Player toPlayer = Bukkit.getPlayer(to);

        boolean hayConversacinEntreSenderYTo = conversacionesWebMySQL.hayConversacionEntre(sender, to);

        if(!hayConversacinEntreSenderYTo){
            ConversacionWeb antiguaConversacionServer = conversacionesWebMySQL.getConversacionServer(to);

            if(antiguaConversacionServer != null){
                conversacionesWebMySQL.borrarConversacionServer(to);
                conversacionesWebMySQL.cerrarChat(antiguaConversacionServer);
            }

            conversacionesWebMySQL.nuevaConversacion(sender, to);

            toPlayer.sendMessage(ChatColor.DARK_AQUA + "Te han enviado un mensaje desde la web. Para responder:  /re <mensaje>");
        }

        toPlayer.sendMessage(ChatColor.BOLD + "" + ChatColor.BOLD + sender + "> " + ChatColor.RESET + "" + ChatColor.YELLOW + message);
        toPlayer.playSound(toPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);

        return NO_RESPONSE;
    }

}
