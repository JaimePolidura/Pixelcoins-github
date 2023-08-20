package es.serversurvival.minecraftserver.lootbox.comprar;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.lootbox.VerTierLootboxMenu;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class OpcionesLootboxMenu extends Menu<LootboxTier> {
    private final Configuration configuration;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 3, 0, 4}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(MenuItems.TITULO_MENU + "Opciones lootbox: " + getState().name())
                .item(1, this::buildItemInfo)
                .item(2, this::buildItemComprar, this::comprarLootbox)
                .item(3, this::buildItemVerItemsLootbox, (p, e) -> menuService.open(p, VerItemsLootbox.class, getState()))
                .item(4, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, VerTierLootboxMenu.class))
                .build();
    }

    private ItemStack buildItemVerItemsLootbox(Player player) {
        return ItemBuilder.of(Material.BOOKSHELF)
                .title(MenuItems.CLICKEABLE + "VER ITEMS")
                .build();
    }

    private void comprarLootbox(Player player, InventoryClickEvent event) {
        menuService.open(player, ConfirmacionComprarLootboxMenu.class, getState());
    }

    private ItemStack buildItemComprar(Player player) {
        return ItemBuilder.of(Material.GOLD_INGOT)
                .title(MenuItems.CLICKEABLE + "COMPRAR")
                .lore(List.of(
                        GOLD + "Precio: " + Funciones.formatPixelcoins(configuration.getDouble(getState().getConfigurationKey())) + "/ lootbox"
                ))
                .build();

    }

    private ItemStack buildItemInfo(Player player) {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Puedes comprar una lootbox, que si la abres",
                        GOLD + "te da un item aleatorio. Dependiendo del tier",
                        GOLD + "te podra tocar mejores o peores items"
                ))
                .build();
    }
}
