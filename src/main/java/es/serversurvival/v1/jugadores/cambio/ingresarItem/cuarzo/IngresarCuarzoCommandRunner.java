package es.serversurvival.v1.jugadores.cambio.ingresarItem.cuarzo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.v1.jugadores.cambio.ingresarItem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio cuarzo",
        explanation = "Cambiar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class IngresarCuarzoCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(CommandSender sender) {
        this.ingresadorItem.ingresarItemInMano((Player) sender, TipoCambioPixelcoins.QUARTZ_BLOCK);
    }
}
