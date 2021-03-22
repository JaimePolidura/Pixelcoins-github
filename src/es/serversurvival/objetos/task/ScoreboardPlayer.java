package es.serversurvival.objetos.task;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Deudas;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardPlayer extends BukkitRunnable {
    public static final int scoreboardSwitchDelay = 60;
    private static STATE state = STATE.START;
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    private enum STATE {
        PLAYER_STATS, PLAYER_DEUDAS, TOP_PLAYERS, START;
    }

    @Override
    public void run() {
        switch (state) {
            case PLAYER_STATS:
                state = STATE.TOP_PLAYERS;
                break;
            case TOP_PLAYERS:
                state = STATE.PLAYER_DEUDAS;
                break;
            case PLAYER_DEUDAS:
            case START:
                state = STATE.PLAYER_STATS;
                break;
        }
        this.updateAll(state);
    }

    public void updateScoreboard(Player p) {
        Scoreboard scoreboard = null;
        switch (state) {
            case PLAYER_STATS:
                scoreboard = this.createScoreboardStats(p.getName());
                break;
            case TOP_PLAYERS:
                scoreboard = this.createScoreboardTopPlayers();
                break;
            case PLAYER_DEUDAS:
                scoreboard = this.createScoreboardDeudas(p.getName());
                break;
        }
        p.setScoreboard(scoreboard);
    }

    private void updateAll(STATE state) {
        Scoreboard scoreboard = null;

        if (state == STATE.TOP_PLAYERS) {
            scoreboard = this.createScoreboardTopPlayers();
        }

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            switch (state) {
                case PLAYER_STATS:
                    scoreboard = this.createScoreboardStats(p.getName());
                    break;
                case PLAYER_DEUDAS:
                    scoreboard = this.createScoreboardDeudas(p.getName());
                    break;
            }
            p.setScoreboard(scoreboard);
        }
    }

    private Scoreboard createScoreboardStats(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dinero", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "JUGADOR");

        double dineroJugador = 0;
        Jugador j = new Jugador();

        try {
            j.conectar();
            dineroJugador = j.getDinero(jugador);
            j.desconectar();
        } catch (Exception e) {
        }

        Score score1 = objective.getScore(ChatColor.GOLD + "Tu dinero: " + ChatColor.GREEN + formatea.format(dineroJugador) + " PC");
        score1.setScore(1);

        Score score2 = objective.getScore("    ");
        score2.setScore(0);

        Score score3 = objective.getScore(ChatColor.GOLD + "-----------------------");
        score3.setScore(-2);

        Empresas em = new Empresas();
        Funciones f = new Funciones();
        ArrayList<String> empresas;

        try {
            em.conectar();
            empresas = em.getNombreEmpresasOwner(jugador);

            String mensaje4;
            double pixelcoinsEmpresa;
            int fila = -2;
            double rentabilidad;
            double ingresos;
            double gastos;
            double beneficios;
            Score score;

            for (String empresa : empresas) {
                mensaje4 = "";

                pixelcoinsEmpresa = em.getPixelcoins(empresa);
                ingresos = em.getIngresos(empresa);
                gastos = em.getGastos(empresa);
                beneficios = ingresos - gastos;

                mensaje4 = ChatColor.GOLD + "- " + empresa + " (" + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa) + " PC ";
                rentabilidad = f.rentabilidad(ingresos, beneficios);

                if (rentabilidad < 0) {
                    mensaje4 = mensaje4 + ChatColor.RED + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
                } else {
                    mensaje4 = mensaje4 + ChatColor.GREEN + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
                }

                if (mensaje4.toCharArray().length > 40) {
                    mensaje4 = ChatColor.GOLD + "- " + empresa + " (" + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa) + " PC";
                    if (mensaje4.toCharArray().length > 40) {
                        mensaje4 = ChatColor.GOLD + "- " + empresa;
                    }
                }
                fila = fila - 1;
                score = objective.getScore(mensaje4);

                score.setScore(fila);
            }
            em.desconectar();
        } catch (Exception e) {

        }
        return scoreboard;
    }

    private Scoreboard createScoreboardTopPlayers() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("topjugadores", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TOP JUGADORES");

        Jugador j = new Jugador();
        j.conectar();
        HashMap<String, Double> jugadores = j.getTop3PlayersPixelcoins();

        String mensaje;
        int pixelcoins;
        Score score;
        int fila = 0;
        int pos = 1;

        for (Map.Entry<String, Double> entry : jugadores.entrySet()) {
            mensaje = ChatColor.GOLD + "" + pos + ": " + entry.getKey() + ChatColor.GREEN + " " + formatea.format(entry.getValue()) + " PC";
            score = objective.getScore(mensaje);
            score.setScore(fila);
            fila = fila - 1;
            pos++;
        }
        j.desconectar();

        return scoreboard;
    }

    private Scoreboard createScoreboardDeudas(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("deudas", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");

        Deudas d = new Deudas();
        Jugador j = new Jugador();
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
