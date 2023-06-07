package es.serversurvival.minecraftserver.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface ServerScoreboardCreator {
    boolean isGlobal();

    Scoreboard create(Player player);
}
