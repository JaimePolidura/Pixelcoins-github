package es.serversurvival.webconnection.conversacionesweb.eventlisteners;

import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionWeb;
import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class OnPlayerQuitEvent implements Listener {
    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;

        ConversacionWeb conversacionWeb = conversacionesWebMySQL.getConversacionServer(playerName);

        if(conversacionWeb != null){
            conversacionesWebMySQL.cerrarChat(conversacionWeb);
        }
    }
}
