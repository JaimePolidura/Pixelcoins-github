package es.serversurvival.minecraftserver.lootbox.comprar;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.minecraftserver.lootbox.VerTierLootboxMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "lootbox comprar",
        explanation = "Comprar lootbox"
)
@RequiredArgsConstructor
public final class ComprarLootboxCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerTierLootboxMenu.class);
    }
}
