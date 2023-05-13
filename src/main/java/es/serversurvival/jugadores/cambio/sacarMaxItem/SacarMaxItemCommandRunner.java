package es.serversurvival.jugadores.cambio.sacarMaxItem;

import es.bukkitbettermenus.Menu;
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
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;

        this.menuService.open(player, (Class<? extends Menu<?>>) SacarMaxItemMenu.class);
    }
}
