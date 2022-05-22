package es.serversurvival.deudas.ver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.scoreboards.SingleScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.MinecraftUtils.*;

public class DeudasDisplayScoreboard implements SingleScoreboard {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;

    public DeudasDisplayScoreboard(){
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public Scoreboard createScoreborad(java.lang.String jugador) {
        Scoreboard scoreboard = createScoreboard("deudas", ChatColor.GOLD + "" + ChatColor.BOLD + "DEUDAS");
        Objective objective = scoreboard.getObjective("deudas");

        Jugador jugadorScoreboear = jugadoresService.getByNombre(jugador);
        double totalAPagar = this.deudasService.getAllPixelcoinsDeudasDeudor(jugador);
        double totalASerPagado = this.deudasService.getAllPixelcoinsDeudasDeudor(jugador);

        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que debes: " + ChatColor.GREEN + FORMATEA.format(totalAPagar) + " PC", 0);
        addLineToScoreboard(objective, ChatColor.GOLD + "Pixelcoins que te deben: " + ChatColor.GREEN + FORMATEA.format(totalASerPagado) + " PC", -1);
        addLineToScoreboard(objective, "     ", -2);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces pagadas la deuda: " + FORMATEA.format(jugadorScoreboear.getNPagosDeuda()), -3);
        addLineToScoreboard(objective, ChatColor.GOLD + "Nº de veces no pagadas la deuda: " + FORMATEA.format(jugadorScoreboear.getNInpagosDeuda()), -4);

        return scoreboard;
    }
}
