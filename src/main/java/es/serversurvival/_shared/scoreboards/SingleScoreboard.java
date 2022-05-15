package es.serversurvival._shared.scoreboards;

import org.bukkit.scoreboard.Scoreboard;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances {
    Scoreboard createScoreborad(String player);
}
