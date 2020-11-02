package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

public class DeudasDisplayScoreboard implements SingleScoreboard{
    private Deudas deudasMySQ = Deudas.INSTANCE;
    private Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    private DecimalFormat formatea = Funciones.FORMATEA;

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("deudas", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");

        int totalAPagar = deudasMySQ.getDeudasDeudor(jugador).stream()
                .mapToInt(Deuda::getPixelcoins)
                .sum();

        int totalASerPagado = deudasMySQ.getDeudasAcredor(jugador).stream()
                .mapToInt(Deuda::getPixelcoins)
                .sum();

        Jugador jugadorScoreboear = jugadoresMySQL.getJugador(jugador);

        Score score1 = objective.getScore(ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + formatea.format(totalAPagar) + " PC");
        score1.setScore(0);

        Score score2 = objective.getScore(ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + formatea.format(totalASerPagado) + " PC");
        score2.setScore(-1);

        Score score3 = objective.getScore("    ");
        score3.setScore(-2);

        String score4Mensaje;
        String score5Mensaje;
        if(jugadorScoreboear == null){
            score4Mensaje = ChatColor.GOLD + "Nº de veces pagadas la deuda: 0";
            score5Mensaje = ChatColor.GOLD + "Nº de veces no pagadas la deuda: 0";
        }else{
            score4Mensaje = ChatColor.GOLD + "Nº de veces pagadas la deuda: " + formatea.format(jugadorScoreboear.getNpagos());
            score5Mensaje = ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + formatea.format(jugadorScoreboear.getNinpagos());
        }

        Score score4 = objective.getScore(score4Mensaje);
        score4.setScore(-3);
        Score score5 = objective.getScore(score5Mensaje);
        score5.setScore(-4);

        return scoreboard;
    }
}