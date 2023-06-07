package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.v1.jugadores._shared.application.CalculadorPatrimonio;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;

@ScoreboardCreator
@RequiredArgsConstructor
public class TopPlayerDisplayScoreboard implements ServerScoreboardCreator {
    private final CalculadorPatrimonio calculadorPatrimonio;

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("topjugadores", ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");
        Objective objective = scoreboard.getObjective("topjugadores");

        Map<String, Double> topPlayers = calculadorPatrimonio.calcularTopJugadores(false, 5);

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
