package es.serversurvival.objetos.scoreboeards;

import es.serversurvival.objetos.mySQL.Jugadores;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Map;

public class TopPlayerDisplayScoreboard implements GlobalScoreboardAnticipable{
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    @Override
    public <T, K> Scoreboard createScoreboard(Map<T, K> mapa) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("topjugadores", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");

        Jugadores j = new Jugadores();
        j.conectar();

        String mensaje;
        Score score;
        int fila = 0;
        int pos = 1;

        for (Map.Entry<T, K> entry : mapa.entrySet()) {
            if(pos >= 4) break;

            mensaje = ChatColor.GOLD + "" + pos + ": " + entry.getKey() + ChatColor.GREEN + " " + formatea.format(entry.getValue()) + " PC";
            score = objective.getScore(mensaje);
            score.setScore(fila);
            fila = fila - 1;
            pos++;
        }
        j.desconectar();

        return scoreboard;
    }
}
