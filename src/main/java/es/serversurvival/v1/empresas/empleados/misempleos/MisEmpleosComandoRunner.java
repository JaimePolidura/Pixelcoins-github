package es.serversurvival.v1.empresas.empleados.misempleos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "empleos misempleos", explanation = "Ver todos los empleos en los que estes contratado")
@AllArgsConstructor
public class MisEmpleosComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, (Class<? extends Menu<?>>) VerEmpleosMenu.class);
    }
}
