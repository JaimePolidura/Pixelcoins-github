package es.serversurvival.minecraftserver.lootbox.mislootboxes;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "lootbox ver",
        explanation = "Ver tus lootboxes"
)
@RequiredArgsConstructor
public final class VerMisLootboxesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerMisLootboxesMenu.class);
    }
}
