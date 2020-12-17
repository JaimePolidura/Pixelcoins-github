package es.serversurvival.eventosMinecraft;

import es.serversurvival.menus.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent event) {
        MenuManager.borrarMenu(event.getPlayer().getName());
    }
}
