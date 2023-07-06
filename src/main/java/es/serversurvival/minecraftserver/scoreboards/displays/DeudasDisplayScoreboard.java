package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@ScoreboardCreator
@RequiredArgsConstructor
public class DeudasDisplayScoreboard implements ServerScoreboardCreator {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;
    private final DeudasService deudasService;

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = createScoreboard("deudas", GOLD + "" + BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        JugadorEstadisticas estadisticas = jugadoresEstadisticasService.getById(player.getUniqueId());
        double totalAPagar = getPixelcoinsTotalesDeudor(player);
        double totalASerPagado = getPixelcoinsTotalesCredor(player);

        setLineToScoreboard(objective, GOLD + "Pixelcoins que debes: " + formatPixelcoins(totalAPagar), 0);
        setLineToScoreboard(objective, GOLD + "Pixelcoins que te deben: " + formatPixelcoins(totalASerPagado), -1);
        setLineToScoreboard(objective, "     ", -2);
        setLineToScoreboard(objective, GOLD + "Nº de veces pagadas la deuda: " + formatPixelcoins(estadisticas.getNDeudaPagos()), -3);
        setLineToScoreboard(objective, GOLD + "Nº de veces no pagadas la deuda: " + formatPixelcoins(estadisticas.getNDeudaInpagos()), -4);

        return scoreboard;
    }

    private double getPixelcoinsTotalesCredor(Player player) {
        return this.deudasService.findByAcredorJugadorIdPendiente(player.getUniqueId()).stream()
                .mapToDouble(Deuda::getPixelcoinsRestantesDePagar)
                .sum();
    }

    private double getPixelcoinsTotalesDeudor(Player player) {
        return this.deudasService.findByDeudorJugadorIdPendiente(player.getUniqueId()).stream()
                .mapToDouble(Deuda::getPixelcoinsRestantesDePagar)
                .sum();
    }
}
