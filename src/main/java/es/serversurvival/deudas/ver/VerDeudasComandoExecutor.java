package es.serversurvival.deudas.ver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas ver",
        explanation = "Ver todas las deudas que tengas y que tengan contigo"
)
@AllArgsConstructor
public class VerDeudasComandoExecutor implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new VerDeudasMenu(sender.getName()));
    }
}
