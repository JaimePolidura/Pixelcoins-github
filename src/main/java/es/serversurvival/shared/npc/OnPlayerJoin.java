package es.serversurvival.shared.npc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnPlayerJoin implements Listener {
    @EventHandler
    public void on (PlayerJoinEvent event) {
        NPCManager.showPlayer(event.getPlayer());
    }
}
