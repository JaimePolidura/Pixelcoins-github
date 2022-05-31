package es.serversurvival.empresas.empleados.misempleos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
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
