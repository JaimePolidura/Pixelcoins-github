package es.serversurvival.legacy.scoreboeards;

import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival.legacy.util.MinecraftUtils.*;

public class DeudasDisplayScoreboard implements SingleScoreboard {
    @Override
    public Scoreboard createScoreborad(java.lang.String jugador) {
        Scoreboard scoreboard = createScoreboard("deudas", ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        Jugador jugadorScoreboear = jugadoresMySQL.getJugador(jugador);
        int totalAPagar = deudasMySQL.getAllPixelcoinsDeudasDeudor(jugador);
        int totalASerPagado = deudasMySQL.getAllPixelcoinsDeudasAcredor(jugador);

        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + formatea.format(totalAPagar) + " PC", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + formatea.format(totalASerPagado) + " PC", -1);
        addLineToScoreboard(objective, "     ", -2);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces pagadas la deuda: " + formatea.format(jugadorScoreboear.getNpagos()), -3);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + formatea.format(jugadorScoreboear.getNinpagos()), -4);

        return scoreboard;
    }
}
