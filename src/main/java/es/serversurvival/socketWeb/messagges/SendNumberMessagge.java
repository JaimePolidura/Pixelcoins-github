package es.serversurvival.socketWeb.messagges;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Form of the messagge: sendnumber-name=jaimetruman&number=10
 */
public class SendNumberMessagge extends SocketMessaggeExecutor {
    private final String NAME = "sendnumber";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String execute(SocketMessagge messagge) {
        int number = Integer.parseInt(messagge.get("number"));
        Player player = Bukkit.getPlayer(messagge.get("name"));

        if(player != null){
            player.sendMessage(ChatColor.GOLD + "El numero de verificacion: " + number);
        }

        return NO_RETURN_MESSAGGE;
    }
}
