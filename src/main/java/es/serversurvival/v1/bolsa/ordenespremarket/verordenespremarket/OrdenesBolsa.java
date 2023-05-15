package es.serversurvival.v1.bolsa.ordenespremarket.verordenespremarket;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "bolsa ordenes",
        explanation = "Ver todas las ordenes de compra y venta de cantidad pendientes a ejecutar cuando el mercado este cerrado"
)
@AllArgsConstructor
public class OrdenesBolsa implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, BolsaVerOrdernesMenu.class);
    }
}
