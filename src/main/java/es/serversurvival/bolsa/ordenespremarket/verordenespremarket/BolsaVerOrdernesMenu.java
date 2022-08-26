package es.serversurvival.bolsa.ordenespremarket.verordenespremarket;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.ordenespremarket.cancelarorderpremarket.CancelarOrdenUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public final class BolsaVerOrdernesMenu extends Menu {
    private static final String TITLE = DARK_RED + "" + BOLD + "         TUS ORDENES";

    private final OrdenesPremarketService ordenesPremarketService;
    private final CancelarOrdenUseCase cancelarOrdenUseCase;
    private final String jugadorNombre;

    public BolsaVerOrdernesMenu(String jugadorNombre) {
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
        this.cancelarOrdenUseCase = new CancelarOrdenUseCase();
        this.jugadorNombre = jugadorNombre;
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0 },
                {2, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITLE)
                .fixedItems()
                .item(1, buildItemInfo())
                .items(2, buildItemsOrders(), this::cancelOrder)
                .breakpoint(7)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void cancelOrder(Player player, InventoryClickEvent event) {
        UUID orderId = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 6));
        this.cancelarOrdenUseCase.cancelar(player.getName(), orderId);
        player.closeInventory();
        player.sendMessage(GOLD + "Has cancelado la orden");
    }

    private List<ItemStack> buildItemsOrders() {
        return this.ordenesPremarketService.findByJugador(jugadorNombre).stream()
                .map(this::buildItemOrdenPremarket)
                .toList();
    }

    private ItemStack buildItemOrdenPremarket(OrdenPremarket orden) {
        return ItemBuilder.of(Material.NAME_TAG)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA CANCELAR")
                .lore(List.of(
                        "  ",
                        GOLD + "Ticker: " + orden.getNombreActivo(),
                        GOLD + "Cantidad: " + orden.getCantidad(),
                        GOLD + "Operacion: " + orden.getTipoAccion().toString().split("_")[1].toLowerCase(),
                        GOLD + "Tipo: " + orden.getTipoAccion().toString().split("_")[0].toLowerCase(),
                        "  ",
                        "" + orden.getOrderPremarketId()
                ))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "El mercado de cantidad no siempre esta abierto",
                        "Su horario son los dias entre semana 15:30 - 22:30",
                        "Cuando compras una accion fuera de ese horario, se",
                        "añade una orden de compra/venta. Cuando el mercado",
                        "abre se ejecuta"
                ))
                .build();
    }
}
