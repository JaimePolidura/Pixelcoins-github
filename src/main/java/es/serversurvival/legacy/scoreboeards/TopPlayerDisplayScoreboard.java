package es.serversurvival.legacy.scoreboeards;

import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.utils.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Map;

public class TopPlayerDisplayScoreboard implements GlobalScoreboard{
    private DecimalFormat formatea = Funciones.FORMATEA;

    public TopPlayerDisplayScoreboard () {}

    @Override
    public Scoreboard createScorebord() {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("topjugadores", ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");
        Objective objective = scoreboard.getObjective("topjugadores");

        Map<String, Double> topPlayers = Funciones.crearMapaTopPatrimonioPlayers(false);

        int fila = 0;
        int pos = 1;

        for (Map.Entry<String, Double> entry : topPlayers.entrySet()) {
            if(pos >= 4) break;

            String mensaje = ChatColor.GOLD + "" + pos + ": " + entry.getKey() + ChatColor.GREEN + " " + formatea.format(entry.getValue()) + " PC";

            MinecraftUtils.addLineToScoreboard(objective, mensaje, fila);

            fila--;
            pos++;
        }

        return scoreboard;
    }
}
