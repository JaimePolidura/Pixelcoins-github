package es.serversurvival.bolsa.ordenespremarket.verordenespremarket;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "bolsa ordenes",
        explanation = "Ver todas las ordenes de compra y venta de cantidad pendientes a ejecutar cuando el mercado este cerrado"
)
public class OrdenesBolsa implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public OrdenesBolsa() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new BolsaVerOrdernesMenu(sender.getName()));
    }
}
