package es.serversurvival.empresas.accionistasserver.misacciones;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas misacciones",
        explanation = "Ver las cantidad que tengas de las empresas del servidor"
)
public final class MisAccionesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public MisAccionesCommandRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new MisAccionesMenu(sender.getName()));
    }
}
