package es.serversurvival.minecraftserver.lootbox;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "lootbox",
        explanation = "Ver las lootboxes"
)
@RequiredArgsConstructor
public final class LootboxCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerTierLootboxMenu.class);
    }
}
