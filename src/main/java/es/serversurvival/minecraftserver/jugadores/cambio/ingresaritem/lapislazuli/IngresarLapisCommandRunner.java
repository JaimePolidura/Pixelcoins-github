package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.lapislazuli;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "cambio lapis",
        explanation = "Selecciona lapis o bloques de lapis y cambialo por " + TipoCambioPixelcoins.LAPISLAZULI + " PC / Lapis"
)
@AllArgsConstructor
public final class IngresarLapisCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(Player player) {
        this.ingresadorItem.ingresarItemInMano(player, TipoCambioPixelcoins.LAPIS_LAZULI, TipoCambioPixelcoins.LAPIS_BLOCK);
    }
}
