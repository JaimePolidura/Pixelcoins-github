package es.serversurvival.scoreboeards;

import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.util.Funciones;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;

import static es.serversurvival.util.MinecraftUtils.*;

public class DeudasDisplayScoreboard implements SingleScoreboard {
    @Override
    public Scoreboard createScoreborad(String jugador) {
        Scoreboard scoreboard = createScoreboard("deudas", ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        Jugador jugadorScoreboear = jugadoresMySQL.getJugador(jugador);
        int totalAPagar = deudasMySQ.getAllPixelcoinsDeudasDeudor(jugador);
        int totalASerPagado = deudasMySQ.getAllPixelcoinsDeudasAcredor(jugador);

        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + formatea.format(totalAPagar) + " PC", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + formatea.format(totalASerPagado) + " PC", -1);
        addLineToScoreboard(objective, "     ", -2);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces pagadas la deuda: " + formatea.format(jugadorScoreboear.getNpagos()), -3);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + formatea.format(jugadorScoreboear.getNinpagos()), -4);

        return scoreboard;
    }
}
