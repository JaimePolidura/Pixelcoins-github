package es.serversurvival.minecraftserver.deudas.verdeudas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.deudas.vendermercado.PonerVentaDeudaMercadoPrecioSelectorMenu;
import es.serversurvival.minecraftserver.deudas._shared.DeudaItemLore;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.cancelar.CancelarDeudaParametros;
import es.serversurvival.pixelcoins.deudas.pagartodo.PagarTodaLaDeudaParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class OpccionesDeudaMenu extends Menu<Deuda> {
    private final DeudaItemLore deudaItemMercadoLore;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 0, 4, 5}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "      OPCCIONES DEUDA")
                .fixedItems()
                .item(1, this::buildPagarOCancelarDeuda, this::pagarOCancelarDeuda)
                .item(2, this::buildVenderDeudaMeradoItem, this::venderDeudaMercado)
                .item(4, this::buildItemInfo)
                .item(5, MenuItems.GO_BACK, (p, e) -> menuService.open(p, MisDeudasMenu.class))
                .build();
    }

    private ItemStack buildItemInfo(Player player) {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(deudaItemMercadoLore.buildDescDeuda(getState()))
                .build();
    }

    private ItemStack buildVenderDeudaMeradoItem(Player player) {
        return !esAcredor(player) ?
                ItemBuilder.of(Material.AIR).build() :
                ItemBuilder.of(Material.YELLOW_WOOL)
                        .title(MenuItems.CLICKEABLE + "PONER A LA VENTA")
                        .build();
    }

    private void venderDeudaMercado(Player player, InventoryClickEvent event) {
        if(esAcredor(player)){
            menuService.open(player, PonerVentaDeudaMercadoPrecioSelectorMenu.class, getState());
        }
    }

    private ItemStack buildPagarOCancelarDeuda(Player player) {
        return ItemBuilder.of(esAcredor(player) ? Material.GREEN_WOOL : Material.RED_WOOL)
                .title(MenuItems.CLICKEABLE + (esAcredor(player) ? "CANCELAR" : "PAGAR"))
                .build();
    }

    private void pagarOCancelarDeuda(Player player, InventoryClickEvent event) {
        if(esAcredor(player))
            cancelarDeuda(player);
        else
            pagarDeuda(player);
    }

    private void cancelarDeuda(Player player) {
        useCaseBus.handle(CancelarDeudaParametros.of(player.getUniqueId(), getState().getDeudaId()));

        String pixelcoinsDeuda = FORMATEA.format(getState().getPixelcoinsRestantesDePagar());
        player.sendMessage(GOLD + "Has cancelado la deuda");
        player.closeInventory();
        MinecraftUtils.enviarMensajeYSonido(getState().getDeudorJugadorId(),
                player.getName() + " te ha cancelado la deuda que tenia contigo por " + GREEN + pixelcoinsDeuda + " PC",
                Sound.ENTITY_PLAYER_LEVELUP);
    }

    private void pagarDeuda(Player player) {
        useCaseBus.handle(PagarTodaLaDeudaParametros.of(getState().getDeudaId(), player.getUniqueId()));

        String pixelcoinsPagadasFormateadas = FORMATEA.format(getState().getPixelcoinsRestantesDePagar());
        player.sendMessage(GOLD + "Has pagado toda la deuda. En total han sido " + RED + "-" + pixelcoinsPagadasFormateadas + " PC");
        player.closeInventory();
        MinecraftUtils.enviarMensajeYSonido(getState().getAcredorJugadorId(),
                GOLD + player.getName() + " te ha pagada una deuda por " + GREEN + pixelcoinsPagadasFormateadas + " PC",
                Sound.ENTITY_PLAYER_LEVELUP);
    }

    private boolean esAcredor(Player player) {
        return getState().getAcredorJugadorId().equals(player.getUniqueId());
    }
}
