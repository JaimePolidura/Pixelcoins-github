package es.serversurvival.minecraftserver.lootbox;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.lootbox.items.application.LootboxItemsService;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemRareza;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerItemsLootbox extends Menu<LootboxTier> implements AfterShow {
    private final LootboxItemsService lootboxItemsService;
    private final MenuService menuService;

    private Map<LootboxItemRareza, Integer> cantidadItemsPorRareza = new HashMap<>();

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {6, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(MenuItems.TITULO_MENU + "Items: " + getState().name())
                .item(1, this::buildItemInfo)
                .items(6, this::buildItemsLootbox)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, OpcionesLootboxMenu.class, getState()))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, MenuItems.GO_BACKWARD_PAGE)
                        .forward(9, MenuItems.GO_FORWARD_PAGE)
                        .build())
                .build();
    }

    private List<ItemStack> buildItemsLootbox(Player player) {
        return lootboxItemsService.findByTier(getState()).stream()
                .peek(lootbox -> {
                    cantidadItemsPorRareza.putIfAbsent(lootbox.getRareza(), 0);
                    cantidadItemsPorRareza.put(lootbox.getRareza(), cantidadItemsPorRareza.get(lootbox.getRareza()) + 1);
                })
                .map(this::buildItemFromLootboxItem)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromLootboxItem(LootboxItem lootboxItem) {
        return ItemBuilder.of(Material.valueOf(lootboxItem.getNombre()))
                .title(BOLD + buildItemTitleLootbox(lootboxItem))
                .addEnchanments(lootboxItem.getEncantamientos().toEnchantments())
                .amount(lootboxItem.getCantidadMinima())
                .lore(List.of(
                        GOLD + "Probabilidad: " + MenuItems.CARGANDO,
                        GOLD + (lootboxItem.puedeTenerVariasCantidades() ?
                                "Te puede tocar entre: " + lootboxItem.getCantidadMinima() + " y " + lootboxItem.getCantidadMaxima() :
                                "Cantidad: " + lootboxItem.getCantidadMinima()),
                        "",
                        lootboxItem.getRareza().name()
                ))
                .build();
    }

    private String buildItemTitleLootbox(LootboxItem lootboxItem) {
        return switch(lootboxItem.getRareza()) {
            case MUY_COMUN -> GOLD + "MUY COMUN";
            case COMUN -> BLUE + "COMUN";
            case RARO -> RED + "RARO";
            case MUY_RARO -> DARK_RED + "MUY RARO";
        };
    }

    private ItemStack buildItemInfo(Player player) {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Si compras una lootbox con el tier " + getState().name(),
                        GOLD + "Podras recibir uno de estos items.",
                        GOLD + "Dependiendo de la rareza sera mas probable",
                        GOLD + "o menos probable que te toque el item:",
                        GOLD + " - " + LootboxItemRareza.MUY_COMUN + ": " + formatPorcentaje(LootboxItemRareza.MUY_COMUN.getProbabilidad()),
                        GOLD + " - " + LootboxItemRareza.COMUN + ": " + formatPorcentaje(LootboxItemRareza.COMUN.getProbabilidad()),
                        GOLD + " - " + LootboxItemRareza.RARO + ": " + formatPorcentaje(LootboxItemRareza.RARO.getProbabilidad()),
                        GOLD + " - " + LootboxItemRareza.MUY_RARO + ": " + formatPorcentaje(LootboxItemRareza.MUY_RARO.getProbabilidad())
                ))
                .build();
    }

    @Override
    public void afterShow(Player player) {
        super.forEachAllItemsByItemNum(6, (item, pageId, slot) -> {
            LootboxItemRareza rareza = LootboxItemRareza.valueOf(
                    MinecraftUtils.getLastLineOfLoreStr(item, 0)
            );

            int nItemsConLaRareza = cantidadItemsPorRareza.get(rareza);
            double probabilidad = rareza.getProbabilidad() * ((double) 1 / nItemsConLaRareza);

            super.setItemLore(pageId, slot, 0, GOLD + "Probabilidad: " + Funciones.formatPorcentaje(probabilidad));
        });
    }
}

