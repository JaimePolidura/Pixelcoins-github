package es.serversurvival.jugadores.cambio.ingresarItem.lapislazuli;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.jugadores.cambio.ingresarItem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio lapis",
        explanation = "Cambiar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class IngresarLapisCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(CommandSender sender) {
        this.ingresadorItem.ingresarItemInMano((Player) sender, TipoCambioPixelcoins.LAPIS_LAZULI, TipoCambioPixelcoins.LAPIS_BLOCK);
    }
}
