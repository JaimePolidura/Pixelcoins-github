package es.serversurvival.objetos.scoreboeards;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Deudas;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class StatsDisplayScoreboard implements SingleScoreboard{
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dinero", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "JUGADOR");

        double dineroJugador = 0;
        Jugadores j = new Jugadores();

        try {
            j.conectar();
            dineroJugador = j.getDinero(jugador);
            j.desconectar();
        } catch (Exception e) {
        }

        Score score1 = objective.getScore(ChatColor.GOLD + "Tus ahorros: " + ChatColor.GREEN + formatea.format(Math.round(dineroJugador)) + " PC");
        score1.setScore(1);

        Score score2 = objective.getScore("    ");
        score2.setScore(0);

        Score score3 = objective.getScore(ChatColor.GOLD + "-------Empresas-----");
        score3.setScore(-2);

        Empresas em = new Empresas();
        Funciones f = new Funciones();

        try {
            em.conectar();

            //Ordenamos la lista de emprsas a las que tiene mas liquidez
            List<Empresa> empresas = em.getEmpresasOwner(jugador);
            Collections.sort(empresas, (o1, o2) -> {
                if (o1.getPixelcoins() >= o2.getPixelcoins())
                    return -1;
                else
                    return 1;
            });

            String mensaje4;
            double pixelcoinsEmpresa;
            int fila = -2;
            double rentabilidad;
            double ingresos;
            double gastos;
            double beneficios;
            Score score;

            for (Empresa empresa : empresas) {
                pixelcoinsEmpresa = empresa.getPixelcoins();
                ingresos = empresa.getIngresos();
                gastos = empresa.getGastos();
                beneficios = ingresos - gastos;

                mensaje4 = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa) + " PC ";
                rentabilidad = f.rentabilidad(ingresos, beneficios);

                if (rentabilidad < 0) {
                    mensaje4 = mensaje4 + ChatColor.RED + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
                } else {
                    mensaje4 = mensaje4 + ChatColor.GREEN + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
                }

                if (mensaje4.toCharArray().length > 40) {
                    mensaje4 = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa) + " PC";
                    if (mensaje4.toCharArray().length > 40) {
                        mensaje4 = ChatColor.GOLD + "- " + empresa.getNombre();
                    }
                }
                fila--;
                score = objective.getScore(mensaje4);

                score.setScore(fila);
            }
            em.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreboard;
    }
}