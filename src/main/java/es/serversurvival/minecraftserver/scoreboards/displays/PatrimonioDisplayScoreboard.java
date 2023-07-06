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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@ScoreboardCreator
@RequiredArgsConstructor
public class PatrimonioDisplayScoreboard implements ServerScoreboardCreator {
    private final CalculadorPatrimonioService calculadorPatrimonioService;

    private final Map<UUID, Map<TipoCuentaPatrimonio, Double>> patrimonioByJugadorId = new ConcurrentHashMap<>();

    @Override
    public Scoreboard create(Player player) {
        Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado = calculadorPatrimonioService.calcularDesglosadoPorCuentas(player.getUniqueId());

        return creatPatrimonioScoreboard(patrimonioDesglosado, player, "dinero");
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    public void updateTipoCuenta(Player player, TipoCuentaPatrimonio tipoCuenta, double delta) {
        Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado =  patrimonioByJugadorId.get(player.getUniqueId());
        patrimonioDesglosado.put(tipoCuenta, patrimonioDesglosado.get(tipoCuenta) + delta);

        Scoreboard scoreboard = creatPatrimonioScoreboard(patrimonioDesglosado, player, "patrimonio");
        player.setScoreboard(scoreboard);
    }

    private Scoreboard creatPatrimonioScoreboard(Map<TipoCuentaPatrimonio, Double> patrimonioDesglosado, Player player, String nombre) {
        Scoreboard scoreboard = createScoreboard(nombre, GOLD + "" + BOLD + "JUGADOR");
        Objective objective = scoreboard.getObjective(nombre);

        patrimonioByJugadorId.put(player.getUniqueId(), patrimonioDesglosado);

        double patriominioTotal = patrimonioDesglosado.values().stream().mapToDouble(a -> a).sum();

        setLineToScoreboard(objective, GOLD + "Tu patrimonio total: " + formatPixelcoins(Math.round(patriominioTotal)), 1);
        setLineToScoreboard(objective, GOLD + "----------------", 2);
        int scoreBoardLine = 3 + patrimonioDesglosado.size();

        for (TipoCuentaPatrimonio cuenta : patrimonioDesglosado.keySet()) {
            double patriomnioCuenta = patrimonioDesglosado.get(cuenta);

            setLineToScoreboard(objective, getTipoCuentaLineaScoreboard(cuenta, patriomnioCuenta), scoreBoardLine);

            scoreBoardLine--;
        }

        return scoreboard;
    }

    private String getTipoCuentaLineaScoreboard(TipoCuentaPatrimonio cuenta, double patriomnioCuenta) {
        return GOLD + cuenta.getAlias() + ": " + formatPixelcoins(Math.round(patriomnioCuenta));
    }
}
