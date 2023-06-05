package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacarmaxitem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacarmax",
        explanation = "Sacar el numero maximo de objetos por pixelcoins"
)
@AllArgsConstructor
public final class SacarMaxItemCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        this.menuService.open(player, SacarMaxItemMenu.class);
    }
}
