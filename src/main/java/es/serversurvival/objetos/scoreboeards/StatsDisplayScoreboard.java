package es.serversurvival.objetos.scoreboeards;

import es.serversurvival.main.Funciones;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsDisplayScoreboard implements SingleScoreboard{
    private Empresas empresasMySQL = new Empresas();
    private Jugadores jugadoresMySQL = new Jugadores();
    private DecimalFormat formatea = new DecimalFormat("###,###.##");

    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dinero", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "JUGADOR");

        double dineroJugador = jugadoresMySQL.getDinero(jugador);

        Score score1 = objective.getScore(ChatColor.GOLD + "Tus ahorros: " + ChatColor.GREEN + formatea.format(Math.round(dineroJugador)) + " PC");
        score1.setScore(1);

        Score score2 = objective.getScore("    ");
        score2.setScore(0);

        Score score3 = objective.getScore(ChatColor.GOLD + "-------Empresas-----");
        score3.setScore(-2);

        List<Empresa> empresas = sortEmpresaByPixelcoins(empresasMySQL.getEmpresasOwner(jugador));
        AtomicInteger fila = new AtomicInteger(-20);
        empresas.forEach( (empresa) -> {
            String mensaje = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC ";
            mensaje = mensaje + calcularRentabilidadEmpresaYFormatear(empresa);
            mensaje = cambiarLongitudDelMensajeSiEsNecesario(mensaje, empresa);

            Score score = objective.getScore(mensaje);
            score.setScore(fila.get());
            fila.getAndDecrement();
        });

        return scoreboard;
    }

    private String cambiarLongitudDelMensajeSiEsNecesario (String mensaje, Empresa empresa) {
        if (mensaje.length() > 40) {
            mensaje = ChatColor.GOLD + "- " + empresa.getNombre() + " (" + ChatColor.GREEN + formatea.format(empresa.getPixelcoins()) + " PC";
            if (mensaje.length() > 40) {
                mensaje = ChatColor.GOLD + "- " + empresa.getNombre();
            }
        }
        return mensaje;
    }

    private String calcularRentabilidadEmpresaYFormatear (Empresa empresa) {
        double rentabilidad = Funciones.rentabilidad(empresa.getIngresos(), empresa.getIngresos() - empresa.getGastos());

        if(rentabilidad < 0){
            return ChatColor.RED + "" + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
        }else{
            return ChatColor.GREEN + "" + (int) rentabilidad + "%" + ChatColor.GOLD + " )";
        }
    }

    private List<Empresa> sortEmpresaByPixelcoins (List<Empresa> empresas) {
        empresas.sort((o1, o2) -> {
            if (o1.getPixelcoins() >= o2.getPixelcoins())
                return -1;
            else
                return 1;
        });
        return empresas;
    }
}