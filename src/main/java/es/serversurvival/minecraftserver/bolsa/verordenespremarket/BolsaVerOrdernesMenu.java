package es.serversurvival.minecraftserver.bolsa.verordenespremarket;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.pixelcoins.bolsa.cancelarordenpremarket.CancelarOrdenPremarketParametros;
import es.serversurvival.pixelcoins.bolsa.cancelarordenpremarket.CancelarOrdenPremarketUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class BolsaVerOrdernesMenu extends Menu {
    private final OrdenesPremarketService ordenesPremarketService;
    private final ActivosBolsaService activosBolsaService;
    private final UseCaseBus useCaseBus;

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
                .title(DARK_RED + "" + BOLD + "         TUS ORDENES")
                .fixedItems()
                .item(1, buildItemInfo())
                .items(2, this::buildItemsOrders, this::cancelOrder)
                .breakpoint(7)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void cancelOrder(Player player, InventoryClickEvent event) {
        UUID ordenId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);

        useCaseBus.handle(new CancelarOrdenPremarketParametros(player.getUniqueId(), ordenId));
        player.closeInventory();
        player.sendMessage(GOLD + "Has cancelado la orden");
    }

    private List<ItemStack> buildItemsOrders(Player player) {
        return this.ordenesPremarketService.findByJugadorId(player.getUniqueId()).stream()
                .map(this::buildItemOrdenPremarket)
                .toList();
    }

    private ItemStack buildItemOrdenPremarket(OrdenPremarket orden) {
        ActivoBolsa activoBolsa = activosBolsaService.getById(orden.getActivoBolsaId());

        return ItemBuilder.of(Material.NAME_TAG)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA CANCELAR")
                .lore(List.of(
                        "  ",
                        GOLD + "Nombre: " + activoBolsa.getNombreLargo(),
                        GOLD + "Ticker: " + activoBolsa.getNombreCorto(),
                        GOLD + "Cantidad: " + orden.getCantidad(),
                        GOLD + "Tipo: " + orden.getTipoPosicion().toString(),
                        "  ",
                        orden.getOrdenPremarketId().toString()
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
