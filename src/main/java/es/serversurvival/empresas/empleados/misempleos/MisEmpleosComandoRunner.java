package es.serversurvival.empresas.empleados.misempleos;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "empleos misempleos", explanation = "Ver todos los empleos en los que estes contratado")
public class MisEmpleosComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public MisEmpleosComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new VerEmpleosMenu((Player) sender));
    }
}
