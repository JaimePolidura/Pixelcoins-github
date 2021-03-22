package es.serversurvival.objetos.scoreboeards;

import es.serversurvival.objetos.mySQL.Deudas;
import es.serversurvival.objetos.mySQL.Jugadores;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

public class DeudasDisplayScoreboard implements SingleScoreboard{
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("deudas", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");

        Deudas d = new Deudas();
        Jugadores j = new Jugadores();
        try {
            d.conectar();

            int totalAPagar = d.getTodaDeudaDeudor(jugador);
            int totalASerPagado = d.getTodaDeudaAcredor(jugador);

            int npagos = j.getNpagos(jugador);
            int ninpagos = j.getNinpago(jugador);


            Score score1 = objective.getScore(ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + formatea.format(totalAPagar) + " PC");
            score1.setScore(0);

            Score score2 = objective.getScore(ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + formatea.format(totalASerPagado) + " PC");
            score2.setScore(-1);

            Score score3 = objective.getScore("    ");
            score3.setScore(-2);

            Score score4 = objective.getScore(ChatColor.GOLD + "Nº de veces pagadas la deuda: " + formatea.format(npagos));
            score4.setScore(-3);

            Score score5 = objective.getScore(ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + formatea.format(ninpagos));
            score5.setScore(-4);

            d.desconectar();
        } catch (Exception e) {

        }

        return scoreboard;
    }
}