package es.serversurvival.eventosMinecraft;

import es.serversurvival.menus.MenuManager;
import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
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
