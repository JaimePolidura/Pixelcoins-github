package es.serversurvival.v2.minecraftserver.deudas.verdeudas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v2.minecraftserver.deudas._shared.DeudaItemMercadoLore;
import es.serversurvival.v2.minecraftserver.deudas.vendermercado.PonerVentaDeudaMercadoPrecioSelectorMenu;
import es.serversurvival.v2.minecraftserver.deudas.verdeudas.VerMisDeudasMenu;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas.cancelar.CancelarDeudaParametros;
import es.serversurvival.v2.pixelcoins.deudas.cancelar.CancelarDeudaUseCase;
import es.serversurvival.v2.pixelcoins.deudas.pagartodo.PagarTodaLaDeudaParametros;
import es.serversurvival.v2.pixelcoins.deudas.pagartodo.PagarTodaLaDeudaUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class OpccionesDeudaMenu extends Menu<Deuda> {
    private final PagarTodaLaDeudaUseCase pagarTodaLaDeudaUseCase;
    private final CancelarDeudaUseCase cancelarDeudaUseCase;
    private final DeudaItemMercadoLore deudaItemMercadoLore;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 0, 4, 5}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "        DEUDA")
                .fixedItems()
                .item(5, Material.GREEN_BANNER, (p, e) -> menuService.open(p, VerMisDeudasMenu.class))
                .item(1, this::buildPagarOCancelarDeuda, this::pagarOCancelarDeuda)
                .item(2, this::buildVenderDeudaMeradoItem, this::venderDeudaMercado)
                .item(4, this::buildItemInfo)
                .build();
    }

    private ItemStack buildItemInfo(Player player) {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(deudaItemMercadoLore.build(getState()))
                .build();
    }

    private ItemStack buildVenderDeudaMeradoItem(Player player) {
        return !esAcredor(player) ?
                ItemBuilder.of(Material.AIR).build() :
                ItemBuilder.of(Material.YELLOW_WOOL)
                        .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA PONER A LA VENTA")
                        .build();
    }

    private void venderDeudaMercado(Player player, InventoryClickEvent event) {
        if(esAcredor(player)){
            menuService.open(player, PonerVentaDeudaMercadoPrecioSelectorMenu.class, getState());
        }
    }

    private ItemStack buildPagarOCancelarDeuda(Player player) {
        return ItemBuilder.of(esAcredor(player) ? Material.GREEN_WOOL : Material.RED_WOOL)
                .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA " + (esAcredor(player) ? "CANCELAR" : "PAGAR"))
                .build();
    }

    private void pagarOCancelarDeuda(Player player, InventoryClickEvent event) {
        if(esAcredor(player))
            pagarDeuda(player);
        else
            cancelarDeuda(player);
    }

    private void cancelarDeuda(Player player) {
        cancelarDeudaUseCase.cancelar(CancelarDeudaParametros.of(player.getUniqueId(), getState().getDeudaId()));
        player.sendMessage(GOLD + "Has cancelado la deuda");
        player.closeInventory();
    }

    private void pagarDeuda(Player player) {
        pagarTodaLaDeudaUseCase.pagarTodaLaDeuda(PagarTodaLaDeudaParametros.of(getState().getDeudaId(), player.getUniqueId()));
        player.sendMessage(GOLD + "Has pagado toda la deuda. En total han sido -" + RED + FORMATEA.format(getState().getPixelcoinsRestantesDePagar()) + " PC");
        player.closeInventory();
    }

    private boolean esAcredor(Player player) {
        return getState().getAcredorJugadorId().equals(player.getUniqueId());
    }
}
