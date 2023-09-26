package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.createScoreboard;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.setLineToScoreboard;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;

@ScoreboardCreator
@RequiredArgsConstructor
public final class CambioDisplayScoreboard implements ServerScoreboardCreator {
    private final Configuration configuration;

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = createScoreboard("cambio", GOLD + "" + BOLD + "CAMBIO");
        Objective objective = scoreboard.getObjective("cambio");
        int fila = 0;

        for (TipoCambioPixelcoins tipoCambio : TipoCambioPixelcoins.values()) {
            double cambio = configuration.getDouble(tipoCambio.cambioConfigKey);
            setLineToScoreboard(objective, GOLD + tipoCambio.getNombre() + " -> " + Funciones.formatPixelcoins(cambio * tipoCambio.cantidad), fila);
            fila = fila - 1;
        }

        return scoreboard;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
