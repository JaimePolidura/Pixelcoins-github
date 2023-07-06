package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival._shared.utils.Funciones.formatRentabilidad;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;

@ScoreboardCreator
@RequiredArgsConstructor
public class TopPlayerDisplayScoreboard implements ServerScoreboardCreator {
    private final CalculadorPatrimonioService calculadorPatrimonioService;

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("topjugadores", ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");
        Objective objective = scoreboard.getObjective("topjugadores");

        Map<String, Double> topPlayers = calculadorPatrimonioService.calcularTopJugadores(false, 5);

        int fila = 0;
        int posicionTopReico = 1;

        for (Map.Entry<String, Double> entry : topPlayers.entrySet()) {
            String mensaje = ChatColor.GOLD + "" + posicionTopReico + ": " + entry.getKey() + " " + formatPixelcoins(entry.getValue());

            MinecraftUtils.setLineToScoreboard(objective, mensaje, fila);

            fila--;
            posicionTopReico++;
        }

        return scoreboard;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
