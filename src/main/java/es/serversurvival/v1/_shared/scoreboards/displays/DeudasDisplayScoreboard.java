package es.serversurvival.v1._shared.scoreboards.displays;

import es.serversurvival.v1._shared.scoreboards.ScoreboardCreator;
import es.serversurvival.v1._shared.scoreboards.ServerScoreboardCreator;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.*;

@ScoreboardCreator
@RequiredArgsConstructor
public class DeudasDisplayScoreboard implements ServerScoreboardCreator {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = createScoreboard("deudas", ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        Jugador jugadorScoreboear = jugadoresService.getByNombre(player.getName());
        double totalAPagar = this.deudasService.getAllPixelcoinsDeudasDeudor(player.getName());
        double totalASerPagado = this.deudasService.getAllPixelcoinsDeudasDeudor(player.getName());

        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + FORMATEA.format(totalAPagar) + " PC", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + FORMATEA.format(totalASerPagado) + " PC", -1);
        addLineToScoreboard(objective, "     ", -2);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces pagadas la deuda: " + FORMATEA.format(jugadorScoreboear.getNpagosDeuda()), -3);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + FORMATEA.format(jugadorScoreboear.getNinpagosDeuda()), -4);

        return scoreboard;
    }
}
