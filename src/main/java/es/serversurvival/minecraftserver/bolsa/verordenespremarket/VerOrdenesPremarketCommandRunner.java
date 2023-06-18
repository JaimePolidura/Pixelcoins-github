package es.serversurvival.minecraftserver.bolsa.verordenespremarket;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "bolsa ordenespremarket",
        explanation = "Ver todas las ordenes pendientes en bolsa cuando el mercado esta cerrado"
)
@RequiredArgsConstructor
public final class VerOrdenesPremarketCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, BolsaVerOrdernesMenu.class);
    }
}
