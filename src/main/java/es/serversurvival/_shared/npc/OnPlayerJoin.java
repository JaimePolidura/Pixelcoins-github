package es.serversurvival._shared.npc;

import es.dependencyinjector.annotations.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
public final class OnPlayerJoin implements Listener {
    @EventHandler
    public void on (PlayerJoinEvent event) {
        NPCManager.showPlayer(event.getPlayer());
    }
}
