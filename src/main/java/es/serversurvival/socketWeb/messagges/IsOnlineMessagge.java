package es.serversurvival.socketWeb.messagges;

import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sun.misc.Queue;

/**
 * isonline-name=jaimetruman
 */
public class IsOnlineMessagge extends SocketMessaggeExecutor{
    private final String name = "isonline";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String execute(SocketMessagge messagge) {
        String playerName = messagge.get("name");
        Player player = Bukkit.getPlayer(playerName);

        System.out.println(player != null);

        return player != null ? "true" : "false";
    }
}
