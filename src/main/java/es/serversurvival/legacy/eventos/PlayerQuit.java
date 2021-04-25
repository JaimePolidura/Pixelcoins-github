package es.serversurvival.legacy.eventos;

import es.serversurvival.legacy.menus.MenuManager;
import es.serversurvival.nfs.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import es.serversurvival.nfs.webconnection.conversacionesweb.mysql.ConversacionWeb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        MenuManager.borrarMenu(playerName);

        ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;

        ConversacionWeb conversacionWeb = conversacionesWebMySQL.getConversacionServer(playerName);

        if(conversacionWeb != null){
            conversacionesWebMySQL.cerrarChat(conversacionWeb);
        }

    }
}
