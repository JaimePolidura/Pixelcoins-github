package es.serversurvival.v2.minecraftserver.jugadores.patrimonio;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.CalculadorPatrimonioService;
import es.serversurvival.v2.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Command(
        value = "patrimonio",
        explanation = "Ver el patrimonio de un jugador",
        args = {"[jugador]"}
)
@AllArgsConstructor
public final class VerPatrimonioJugadorCommandRunner implements CommandRunnerArgs<VerPatrimonioComando> {
    private final CalculadorPatrimonioService calculadorPatrimonioService;

    @Override
    public void execute(VerPatrimonioComando comando, Player player) {
        String jugadorNombreVerPatrimonio = comando.getJugador() == null ? player.getName() : comando.getJugador().getName();
        UUID jugadorIdVerPatrimonio = comando.getJugador() == null ? player.getUniqueId() : comando.getJugador().getUniqueId();

        Map<TipoCuentaPatrimonio, Double> desglosadoPatrimonio = calculadorPatrimonioService.calcularDesglosadoPorCuentas(jugadorIdVerPatrimonio);

        player.sendMessage(GOLD + "El patrimonio de esta "+jugadorNombreVerPatrimonio+" formado por: ");

        for (Map.Entry<TipoCuentaPatrimonio, Double> entry : desglosadoPatrimonio.entrySet()) {
            TipoCuentaPatrimonio tipoCuenta = entry.getKey();
            double pixelcoins = entry.getValue();

            player.sendMessage(GOLD + " " + tipoCuenta.getAlias() + ": " + (pixelcoins < 0 ? RED : GREEN) + FORMATEA.format(pixelcoins) + " PC");
        }
    }
}
