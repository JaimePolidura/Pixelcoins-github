package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;

@ScoreboardCreator
@RequiredArgsConstructor
public class TopPlayerDisplayScoreboard implements ServerScoreboardCreator {
    private final CalculadorPatrimonioService calculadorPatrimonioService;
    private final Configuration configuration;

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = MinecraftUtils.createScoreboard("topjugadores", ChatColor.GOLD + "" + ChatColor.BOLD + "TOP RICOS");
        Objective objective = scoreboard.getObjective("topjugadores");

        int nPlayersToShow = configuration.getInt(ConfigurationKey.SCOREBOARDS_N_TOP_JUGADORES);

        Map<String, Double> topPlayers = calculadorPatrimonioService.calcularTopJugadores(false, nPlayersToShow);

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
