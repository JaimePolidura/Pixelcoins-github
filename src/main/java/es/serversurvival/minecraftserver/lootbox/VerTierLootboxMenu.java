package es.serversurvival.minecraftserver.lootbox;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class VerTierLootboxMenu extends Menu {
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 2, 0, 3}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .staticMenu()
                .fixedItems()
                .title(MenuItems.TITULO_MENU + "          Lootboxes")
                .item(1, buildItemLootbox(LootboxTier.COMUN), (p, e) -> openOpcionesLootbox(LootboxTier.COMUN, p))
                .item(2, buildItemLootbox(LootboxTier.NORMAL), (p, e) -> openOpcionesLootbox(LootboxTier.NORMAL, p))
                .item(3, buildItemLootbox(LootboxTier.RARO), (p, e) -> openOpcionesLootbox(LootboxTier.RARO, p))
                .build();
    }

    private void openOpcionesLootbox(LootboxTier tier, Player player) {
        menuService.open(player, OpcionesLootboxMenu.class, tier);
    }

    private ItemStack buildItemLootbox(LootboxTier lootboxTier) {
        return ItemBuilder.of(lootboxTier.getItem())
                .title(MenuItems.CLICKEABLE + "VER OPCCIONES TIER: " + lootboxTier.name())
                .build();
    }
}
