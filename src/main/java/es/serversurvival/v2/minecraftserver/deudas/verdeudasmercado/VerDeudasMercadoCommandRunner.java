package es.serversurvival.v2.minecraftserver.deudas.verdeudasmercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas mercado",
        explanation = "Ver el mercado de deuda"
)
@AllArgsConstructor
public final class VerDeudasMercadoCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        menuService.open(player, MercadoDeudaMenu.class, player);
    }
}
