package es.serversurvival._shared.scoreboards;

import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.scoreboard.Scoreboard;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances {
    Scoreboard createScoreborad(String player);
}
