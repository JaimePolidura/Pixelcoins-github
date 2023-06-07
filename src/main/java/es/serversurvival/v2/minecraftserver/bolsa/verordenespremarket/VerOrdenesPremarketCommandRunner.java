package es.serversurvival.v2.minecraftserver.bolsa.verordenespremarket;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("bolsa ordenespremarket")
@RequiredArgsConstructor
public final class VerOrdenesPremarketCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, BolsaVerOrdernesMenu.class);
    }
}
