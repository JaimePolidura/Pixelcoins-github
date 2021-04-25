package es.serversurvival.nfs.webconnection.conversacionesweb.isonline;

import es.serversurvival.nfs.webconnection.socketmessages.CanRedirect;
import es.serversurvival.nfs.webconnection.socketmessages.SocketMessagge;
import es.serversurvival.nfs.webconnection.socketmessages.SocketMessaggeExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * isonline-name=jaimetruman
 */
public class IsOnlineMessagge extends SocketMessaggeExecutor implements CanRedirect {
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
