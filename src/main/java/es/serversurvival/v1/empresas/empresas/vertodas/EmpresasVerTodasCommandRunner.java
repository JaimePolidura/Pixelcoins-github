package es.serversurvival.v1.empresas.empresas.vertodas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v2.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas vertodas",
        explanation = "Ver todas las empresas del servidor"
)
@RequiredArgsConstructor
public final class EmpresasVerTodasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender commandSender) {
        this.menuService.open((Player) commandSender, (Class<? extends Menu<?>>) VerTodasEmpresasMenu.class);
    }
}
