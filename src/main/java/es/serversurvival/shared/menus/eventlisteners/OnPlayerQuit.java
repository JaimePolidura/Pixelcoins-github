package es.serversurvival.shared.menus.eventlisteners;

import es.serversurvival.shared.menus.MenuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class OnPlayerQuit implements Listener {
    @EventHandler
    public void on (PlayerQuitEvent event) {
        MenuManager.borrarMenu(event.getPlayer().getName());
    }
}
