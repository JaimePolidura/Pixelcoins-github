package es.serversurvival.legacy.scoreboeards;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import org.bukkit.scoreboard.Scoreboard;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances {
    Scoreboard createScoreborad(String player);
}
