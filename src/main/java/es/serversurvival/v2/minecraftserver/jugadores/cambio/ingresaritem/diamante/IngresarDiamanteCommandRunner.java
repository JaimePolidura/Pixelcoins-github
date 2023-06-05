package es.serversurvival.v2.minecraftserver.jugadores.cambio.ingresaritem.diamante;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v2.minecraftserver.jugadores.cambio.ingresaritem.IngresadorItem;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio diamante",
        explanation = "Cambiar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class IngresarDiamanteCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(Player player) {
        this.ingresadorItem.ingresarItemInMano(player, TipoCambioPixelcoins.DIAMOND, TipoCambioPixelcoins.DIAMOND_BLOCK);
    }
}
