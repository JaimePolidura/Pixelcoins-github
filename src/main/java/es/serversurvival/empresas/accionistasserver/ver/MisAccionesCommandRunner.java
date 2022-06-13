package es.serversurvival.empresas.accionistasserver.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;

@Command(
        value = "empresas misacciones",
        explanation = "Ver las acciones que tengas de las empresas del servidor"
)
public final class MisAccionesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public MisAccionesCommandRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender commandSender) {

    }
}
