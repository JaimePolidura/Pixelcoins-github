package es.serversurvival.minecraftserver.scoreboards.displays;

import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.scoreboards.ServerScoreboardCreator;
import es.serversurvival.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@ScoreboardCreator
@RequiredArgsConstructor
public class PatrimonioDisplayScoreboard implements ServerScoreboardCreator {
    private final CalculadorPatrimonioService calculadorPatrimonioService;

    @Override
    public Scoreboard create(Player player) {
        Scoreboard scoreboard = createScoreboard("dinero", GOLD + "" + BOLD + "JUGADOR");
        Objective objective = scoreboard.getObjective("dinero");

        Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado = calculadorPatrimonioService.calcularDesglosadoPorCuentas(player.getUniqueId());
        double patriominioTotal = patrimonioDesglosado.values().stream().mapToDouble(a -> a).sum();

        addLineToScoreboard(objective, GOLD + "Tu patrimonio total: " + GREEN + FORMATEA.format(Math.round(patriominioTotal)) + " PC", 1);
        addLineToScoreboard(objective, GOLD + "----------------", 2);
        int scoreBoardLine = 3 + patrimonioDesglosado.size();

        for (TipoCuentaPatrimonio cuenta : patrimonioDesglosado.keySet()) {
            double patriomnioCuenta = patrimonioDesglosado.get(cuenta);

            addLineToScoreboard(objective, GOLD + cuenta.getAlias() + ": " + GREEN + FORMATEA.format(Math.round(patriomnioCuenta)) + " PC", scoreBoardLine);

            scoreBoardLine--;
        }

        return scoreboard;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
