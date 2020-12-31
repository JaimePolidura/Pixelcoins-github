package es.serversurvival.socketWeb.messagges;

import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sun.misc.Queue;

/**
 * isonline-name=jaimetruman
 */
public class IsOnlineMessagge extends SocketMessaggeExecutor implements CanRedirect{
    private final String name = "isonline";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        String playerName = messagge.get("name");
        Player player = Bukkit.getPlayer(playerName);

        StringBuilder builder = new StringBuilder();
        builder.append("isonline-name=").append(playerName).append("&result=").append(player != null);

        return new SocketMessagge(builder.toString());
    }
}
