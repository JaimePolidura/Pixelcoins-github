package es.serversurvival.jugadores.cambio.sacarMaxItem;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacarmax",
        explanation = "Sacar el numero maximo de objetos por pixelcoins"
)
public final class SacarMaxItemCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public SacarMaxItemCommandRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;

        this.menuService.open(player, new SacarMaxItemMenu(player.getName()));
    }
}
