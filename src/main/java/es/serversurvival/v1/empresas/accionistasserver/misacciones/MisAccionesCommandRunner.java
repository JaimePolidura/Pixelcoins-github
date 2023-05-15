package es.serversurvival.v1.empresas.accionistasserver.misacciones;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas misacciones",
        explanation = "Ver las cantidad que tengas de las empresas del servidor"
)
@AllArgsConstructor
public final class MisAccionesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, MisAccionesMenu.class);
    }
}
