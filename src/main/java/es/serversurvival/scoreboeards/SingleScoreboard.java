package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

public interface SingleScoreboard extends ServerScoreboard, AllMySQLTablesInstances{
    Scoreboard createScoreborad(String player);
}
