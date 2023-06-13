package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;

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
        Scoreboard scoreboard = createScoreboard("deudas", ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        JugadorEstadisticas estadisticas = jugadoresEstadisticasService.getById(player.getUniqueId());
        double totalAPagar = getPixelcoinsTotalesDeudor(player);
        double totalASerPagado = getPixelcoinsTotalesCredor(player);

        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + FORMATEA.format(totalAPagar) + " PC", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + FORMATEA.format(totalASerPagado) + " PC", -1);
        addLineToScoreboard(objective, "     ", -2);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces pagadas la deuda: " + FORMATEA.format(estadisticas.getNDeudaPagos()), -3);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + FORMATEA.format(estadisticas.getNDeudaInpagos()), -4);

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
