package es.serversurvival.objetos.scoreboeards;

import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public interface GlobalScoreboardAnticipable {
    <T,K> Scoreboard createScoreboard(Map<T, K> mapa);
}