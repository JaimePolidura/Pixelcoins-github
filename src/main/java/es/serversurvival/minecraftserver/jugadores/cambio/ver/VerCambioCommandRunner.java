package es.serversurvival.minecraftserver.jugadores.cambio.ver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command("cambio ver")
@RequiredArgsConstructor
public final class VerCambioCommandRunner implements CommandRunnerNonArgs {
    private final Configuration configuration;

    @Override
    public void execute(Player player) {
        for (TipoCambioPixelcoins tipoCambio : TipoCambioPixelcoins.values()) {
            double cambio = configuration.getDouble(tipoCambio.cambioConfigKey);
            player.sendMessage(ChatColor.GOLD + tipoCambio.nombre + " -> " + Funciones.formatPixelcoins(cambio * tipoCambio.cantidad));
        }
    }
}
