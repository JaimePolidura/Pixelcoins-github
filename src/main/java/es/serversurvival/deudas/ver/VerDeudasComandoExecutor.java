package es.serversurvival.deudas.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas ver",
        explanation = "Ver todas las deudas que tengas y que tengan contigo"
)
public class VerDeudasComandoExecutor implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public VerDeudasComandoExecutor() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new VerDeudasMenu(sender.getName()));
    }
}
