package es.serversurvival.socketWeb.messagges;

import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.net.Socket;

public class ChatDisconnectMessage extends SocketMessaggeExecutor {
    private final String name = "chatdisconenct";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        Player player = Bukkit.getPlayer(messagge.get("to"));
        String sender = messagge.get("sender");
        conversacionesWebMySQL.borrarConversacionWeb(sender);

        if(player != null) {
            player.sendMessage(ChatColor.DARK_AQUA + sender + " se ha desconectado");
            player.playSound(player.getLocation(), Sound.BLOCK_METAL_HIT, 1, 10);
        }

        return NO_RESPONSE;
    }
}
