package es.serversurvival.empresas.empresas.vertodas;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas vertodas",
        explanation = "Ver todas las empresas del servidor"
)
public final class EmpresasVerTodasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public EmpresasVerTodasCommandRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender commandSender) {
        this.menuService.open((Player) commandSender, new VerTodasEmpresasMenu((Player) commandSender));
    }
}
