package es.serversurvival.minecraftserver.deudas.verdeudas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemLore;
import es.serversurvival.minecraftserver.deudas.verdeudasmercado.MercadoDeudaMenu;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static es.serversurvival._shared.utils.Funciones.formatPorcentaje;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CLICKEABLE;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class MisDeudasMenu extends Menu {
    private final DeudaItemLore deudaItemMercadoLore;
    private final DeudasService deudasService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0, 0, 0, 0},
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
                .title(DARK_RED + "" + BOLD + "        TUS DEUDAS")
                .item(1, buildItemInfo())
                .item(2, buildItemMercado(), (p, e) -> menuService.open(p, MercadoDeudaMenu.class))
                .items(3, this::buildItemsDeudas, this::onDeudaItemClicked)
                .breakpoint(7, MenuItems.GO_BACK, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())

                .build();
    }

    private ItemStack buildItemMercado() {
        return ItemBuilder.of(Material.CHEST)
                .title(CLICKEABLE + "VER MERCADO DEUDA")
                .build();
    }

    private void onDeudaItemClicked(Player player, InventoryClickEvent event) {
        Deuda deuda = deudasService.getById(MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0));
        menuService.open(player, OpccionesDeudaMenu.class, deuda);
    }

    private List<ItemStack> buildItemsDeudas(Player player) {
        return deudasService.findByJugadorIdPendiente(player.getUniqueId()).stream()
                .map(this::buildItemDeuda)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemDeuda(Deuda deuda) {
        boolean esAcredor = deuda.getAcredorJugadorId().equals(getPlayer().getUniqueId());

        return ItemBuilder.of(esAcredor ? Material.GREEN_BANNER : Material.RED_BANNER)
                .title(CLICKEABLE + "VER")
                .lore(deudaItemMercadoLore.buildDescDeuda(deuda))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Puedes dar prestamos a jugadores y endeudarte por PC.",
                        "",
                        GOLD + "El pago del prestamo seran las pixelcoins por los interes. ",
                        GOLD + "Por ejemplo pixelcoins: " + formatPixelcoins(100) + " intereses: " + formatPorcentaje(0.1) + " = " + formatPixelcoins(100 * 0.1),
                        GOLD + "El pago se hara cada X dias en un total de Y veces",
                        GOLD + "Salvo el ultimo dia que se pagará tambien todas las pixelcions.",
                        "",
                        GOLD + "Ejemplo: digamos que los interes son " + formatPixelcoins(10),
                        GOLD + "y tendra un total de 3 pagos que se haran cada dia:",
                        GOLD + "   - Dia 1 " + formatPixelcoins(10),
                        GOLD + "   - Dia 2 " + formatPixelcoins(10),
                        GOLD + "   - Dia 3 " + formatPixelcoins(10) + "+ " + formatPixelcoins(100),
                        GOLD + "Total pagado: " + formatPixelcoins(10 * 4 + 100),
                        "",
                        AQUA + "/deudas prestar",
                        AQUA + "/deudas ver"
                ))
                .build();
    }
}
