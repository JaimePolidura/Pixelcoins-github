package es.serversurvival.v1._shared.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface ServerScoreboardCreator {
    boolean isGlobal();

    Scoreboard create(Player player);
}
