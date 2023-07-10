package es.serversurvival.minecraftserver.lootbox.mislootboxes;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.minecraftserver.lootbox.AbrirLootboxMenu;
import es.serversurvival.minecraftserver.lootbox.VerTierLootboxMenu;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.application.LootboxEnPropiedadService;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxCompradaEstado;
import es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain.LootboxEnPropiedad;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerMisLootboxesMenu extends Menu {
    private final LootboxEnPropiedadService lootboxEnPropiedadService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(MenuItems.TITULO_MENU + "      Tus lootboxes")
                .fixedItems()
                .item(1, this::buildItemInfo)
                .item(2, this::buildItemVerTierLootboxes, (p, e) -> menuService.open(p, VerTierLootboxMenu.class))
                .items(4, this::buildItemLootboxes, this::abrirLootbox)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, MenuItems.GO_BACKWARD_PAGE)
                        .forward(9, MenuItems.GO_FORWARD_PAGE)
                        .build())
                .build();
    }

    private void abrirLootbox(Player player, InventoryClickEvent event) {
        UUID lootboxEnPropiedadId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);

        menuService.open(player, AbrirLootboxMenu.class, lootboxEnPropiedadId);
    }

    private List<ItemStack> buildItemLootboxes(Player player) {
        return lootboxEnPropiedadService.findByJugadorAndEstado(player.getUniqueId(), LootboxCompradaEstado.PENDIENTE).stream()
                .map(this::buildItemFromLootbox)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromLootbox(LootboxEnPropiedad lootboxEnPropiedad) {
        return ItemBuilder.of(lootboxEnPropiedad.getTier().getItem())
                .title(MenuItems.CLICKEABLE + "ABRIR")
                .lore(List.of(
                        GOLD + "Tier: " + lootboxEnPropiedad.getTier().name(),
                        GOLD + "Fecha compra: " + Funciones.toString(lootboxEnPropiedad.getFechaCompra()),
                        "",
                        lootboxEnPropiedad.getLootboxEnPropiedadId().toString()
                ))
                .build();
    }

    private ItemStack buildItemVerTierLootboxes(Player player) {
        return ItemBuilder.of(Material.BOOKSHELF)
                .title(MenuItems.CLICKEABLE + "COMPRAR LOOTBOXES")
                .build();
    }

    private ItemStack buildItemInfo(Player player) {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Puedes comprar una lootbox por pixelcoins",
                        GOLD + "Al abrirlas te daran un item aleatorio",
                        GOLD + "Lo bueno o lo malo de lo que pueda ser",
                        GOLD + "El item dependera del tier"
                ))
                .build();
    }
}
