package es.serversurvival.v2.minecraftserver.deudas.verdeudas;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas ver",
        explanation = "Ver tus deudas"
)
@AllArgsConstructor
public final class VerMisDeudasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        menuService.open(player, VerMisDeudasMenu.class, player);
    }
}
