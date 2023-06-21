package es.serversurvival.minecraftserver.jugadores.cambio.ingresaritem;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "cambio ingresar",
        explanation = "Selcciona un item en la mano, y se cambiara por pixelcions /cambio ayuda"
)
@AllArgsConstructor
public final class IngresarItemCommandRunner implements CommandRunnerNonArgs {
    private final IngresadorItem ingresadorItem;

    @Override
    public void execute(Player player) {
        ingresadorItem.ingresarItemInMano(player, TipoCambioPixelcoins.values());
    }
}
