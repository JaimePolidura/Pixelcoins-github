package es.serversurvival.scoreboeards;

import org.bukkit.scoreboard.Scoreboard;
import org.omg.PortableServer.ServantRetentionPolicy;

public interface GlobalScoreboard extends ServerScoreboard {
    Scoreboard createScorebord();
}
