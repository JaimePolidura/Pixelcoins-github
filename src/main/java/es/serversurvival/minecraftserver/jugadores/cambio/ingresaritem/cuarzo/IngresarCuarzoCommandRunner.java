package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.cuarzo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "cambio cuarzo",
        explanation = "Cambiar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class IngresarCuarzoCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(Player player) {
        this.ingresadorItem.ingresarItemInMano(player, TipoCambioPixelcoins.QUARTZ_BLOCK);
    }
}
