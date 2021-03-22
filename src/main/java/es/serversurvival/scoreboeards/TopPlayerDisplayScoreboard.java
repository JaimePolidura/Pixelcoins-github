package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Map;

public class TopPlayerDisplayScoreboard implements GlobalScoreboard{
    private Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    private DecimalFormat formatea = Funciones.FORMATEA;

    public TopPlayerDisplayScoreboard () {}

    @Override
    public Scoreboard createScorebord() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("topjugadores", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");

        MySQL.conectar();
        Map<String, Double> topPlayers = Funciones.crearMapaTopPatrimonioPlayers(false);
        MySQL.desconectar();

        int fila = 0;
        int pos = 1;

        for (Map.Entry<String, Double> entry : topPlayers.entrySet()) {
            if(pos >= 4) break;

            String mensaje = ChatColor.GOLD + "" + pos + ": " + entry.getKey() + ChatColor.GREEN + " " + formatea.format(entry.getValue()) + " PC";
            Score score = objective.getScore(mensaje);
            score.setScore(fila);

            fila = fila - 1;
            pos++;
        }

        return scoreboard;
    }
}
