package es.serversurvival.nfs.shared.scoreboards;

import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.scoreboard.Scoreboard;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances {
    Scoreboard createScoreborad(String player);
}
