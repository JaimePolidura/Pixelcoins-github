package es.serversurvival.minecraftserver.transacciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.transacciones.Movimiento;
import es.serversurvival.pixelcoins.transacciones.MovimientosService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerTransaccionesMenu extends Menu<VerTransaccionesMenu.State> {
    private final MovimientosService movimientosService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
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
                .title(MenuItems.TITULO_MENU + "Transacciones " + getState().getNombre())
                .item(1, buildItemInfo())
                .item(2, buidlItemGoBack(), this::goBack)
                .items(6, buildItemsTransacciones())
                .breakpoint(7, MenuItems.GO_MENU_BACK, this::goBack)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, MenuItems.GO_BACKWARD_PAGE)
                        .forward(9, MenuItems.GO_FORWARD_PAGE)
                        .build())
                .build();
    }

    private void goBack(Player player, InventoryClickEvent event) {
        if(getState().onBack != null){
            getState().onBack.run();
        }
    }

    private ItemStack buidlItemGoBack() {
        return getState().getMenuBackItem() != null ?
                getState().getMenuBackItem() :
                ItemBuilder.of(Material.AIR).build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Las transacciones son todos los movimientos de PC:",
                        GOLD + "pagos, gastos etc."
                ))
                .build();
    }

    private List<ItemStack> buildItemsTransacciones() {
        return movimientosService.findByEntidadIdOrderByFecha(getState().entidadId, 100).stream()
                .map(this::buildItemTransaccionFromMovimiento)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemTransaccionFromMovimiento(Movimiento movimiento) {
        boolean esInflow = movimiento.getPixelcoins() > 0;

        return ItemBuilder.of(esInflow ? Material.GREEN_CANDLE : Material.RED_CANDLE)
                .title(MenuItems.TITULO_ITEM + movimiento.getTipo().toString())
                .lore(List.of(
                        GOLD + "Pixelcoins: " + formatPixelcoinsResultado(movimiento.getPixelcoins()),
                        GOLD + "Fecha: " + Funciones.toString(movimiento.getFecha())
                ))
                .build();
    }

    @Builder
    @AllArgsConstructor
    public static class State {
        @Getter private final UUID entidadId;
        @Getter private final String nombre;
        @Getter private final Runnable onBack;
        @Getter private final ItemStack menuBackItem;
    }
}
