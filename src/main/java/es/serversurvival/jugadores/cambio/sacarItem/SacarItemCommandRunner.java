package es.serversurvival.jugadores.cambio.sacarItem;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacar",
        explanation = "Sacar un objeto por pixelcoins"
)
public final class SacarItemCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public SacarItemCommandRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;

        this.menuService.open(player, new SacarItemMenu(player.getName()));
    }
}
