package es.serversurvival.jugadores.top;

import es.serversurvival._shared.scoreboards.GlobalScoreboard;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.MinecraftUtils;
import es.serversurvival.jugadores._shared.application.CalculadorPatrimonio;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;

@RequiredArgsConstructor
public class TopPlayerDisplayScoreboard implements GlobalScoreboard {
    private final CalculadorPatrimonio calculadorPatrimonio;

    @Override
    public Scoreboard createScorebord() {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("topjugadores", ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");
        Objective objective = scoreboard.getObjective("topjugadores");

        Map<String, Double> topPlayers = calculadorPatrimonio.calcularTopJugadores(false);

        int fila = 0;
        int pos = 1;

        for (Map.Entry<String, Double> entry : topPlayers.entrySet()) {
            if(pos >= 4) break;

            String mensaje = ChatColor.GOLD + "" + pos + ": " + entry.getKey() + ChatColor.GREEN + " " + FORMATEA.format(entry.getValue()) + " PC";

            MinecraftUtils.addLineToScoreboard(objective, mensaje, fila);

            fila--;
            pos++;
        }

        return scoreboard;
    }
}
