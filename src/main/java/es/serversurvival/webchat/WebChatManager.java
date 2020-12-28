package es.serversurvival.webchat;

import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class WebChatManager {

    public void handlerMessageFromServer (SocketMessagge socketMessagge) {
        String playerName = socketMessagge.get("to");
        String message = socketMessagge.get("message");
        String sender = socketMessagge.get("sender");

        Player player = Bukkit.getPlayer(playerName);

        if(player != null){
            player.sendMessage(ChatColor.DARK_AQUA + "Te han enviado un mensaje desde la web. Para responder:  /res <mensaje>");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.BOLD + sender + "> " + ChatColor.RESET + "" + ChatColor.YELLOW + message);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }
}
