package es.serversurvival.minecraftserver.scoreboards.listeners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardDisplayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@EventHandler
@RequiredArgsConstructor
public final class OnPlayerJoinScoreboard implements Listener  {
    private final ScoreboardDisplayer scoreboardDisplayer;

    @org.bukkit.event.EventHandler
    public void on(PlayerJoinEvent evento) {
        this.scoreboardDisplayer.showActualScoreboard(evento.getPlayer());
    }
}
