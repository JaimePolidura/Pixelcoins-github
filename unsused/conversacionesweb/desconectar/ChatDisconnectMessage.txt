package es.serversurvival.web.conversacionesweb.desconectar;

import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import es.serversurvival.web.webconnection.socketmessages.SocketMessaggeExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatDisconnectMessage extends SocketMessaggeExecutor {
    private final String name = "chatdisconenct";

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        Player player = Bukkit.getPlayer(messagge.get("to"));
        String senderName = messagge.get("sender");

        this.conversacionesWebService.deleteByWebNombre(senderName);

        if(player != null) {
            player.sendMessage(ChatColor.DARK_AQUA + senderName + " se ha desconectado");
            player.playSound(player.getLocation(), Sound.BLOCK_METAL_HIT, 1, 10);
        }

        return NO_RESPONSE;
    }

    @Override
    public String getName() {
        return name;
    }
}
