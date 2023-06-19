package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.diamante;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem.IngresadorItem;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "cambio diamante",
        explanation = "Selecciona diamantes o bloques de diamante y cambialo por " + TipoCambioPixelcoins.DIAMANTE + " PC / Diamante"
)
@AllArgsConstructor
public final class IngresarDiamanteCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(Player player) {
        this.ingresadorItem.ingresarItemInMano(player, TipoCambioPixelcoins.DIAMOND, TipoCambioPixelcoins.DIAMOND_BLOCK);
    }
}
