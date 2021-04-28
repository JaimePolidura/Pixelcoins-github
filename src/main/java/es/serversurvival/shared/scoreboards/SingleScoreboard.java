package es.serversurvival.shared.scoreboards;

import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.scoreboard.Scoreboard;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances {
    Scoreboard createScoreborad(String player);
}
