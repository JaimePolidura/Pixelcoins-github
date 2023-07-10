package es.serversurvival.minecraftserver.bolsa.vervalores;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.Page;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.async.config.AsyncTasksConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.bukkitbettermenus.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CARGANDO;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerActivosDisponiblesMenu extends Menu<TipoActivoBolsa> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final ActivosBolsaService activosBolsaService;
    private final MovimientosService movimientosService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "   Escoge para invertir")
                .fixedItems()
                .items(1, itemValoresDisponibles(), this::onValorSeleccionado)
                .breakpoint(7)
                .asyncTasks(AsyncTasksConfiguration.builder()
                        .onPageLoaded(1, this::cargarPrecioActualBolsa)
                        .build())
                .paginated(PaginationConfiguration.builder()
                        .backward(8, itemBackPage())
                        .forward(9, itemNextPage())
                        .build())
                .build();
    }

    private void onValorSeleccionado(Player player, InventoryClickEvent event) {
        ItemStack itemClicked = event.getCurrentItem();
        UUID activoBolsaId = MinecraftUtils.getLastLineOfLore(itemClicked, 0);
        ActivoBolsa activoBolsa = activosBolsaService.getById(activoBolsaId);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId, null);
        double pixelcoinsJugador = movimientosService.getBalance(player.getUniqueId());

        if(pixelcoinsJugador < ultimoPrecio){
            MinecraftUtils.enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        menuService.open(player, SeleccionarCantidadComprarBolsaMenu.class, SeleccionarCantidadComprarBolsaMenu.of(
                activoBolsa, ultimoPrecio, pixelcoinsJugador
        ));
    }

    private void cargarPrecioActualBolsa(Page page, int slot, ItemStack item) {
        UUID activoId = MinecraftUtils.getLastLineOfLore(item, 0);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoId, getPlayer().getUniqueId());
        String nuevoLore = GOLD + "Precio: " + formatPixelcoins(ultimoPrecio) + "/ " + getState().getNombreUnidadSingular();

        item = ItemUtils.setLore(item,  1, nuevoLore);

        if(getActualPage().getPageId() == page.getPageId()){
            super.setActualItemLore(slot, 1, nuevoLore );
        }

        super.getActualPage().getItems().set(slot, item);
    }

    private List<ItemStack> itemValoresDisponibles() {
        return activosBolsaService.findByTipo(getState()).stream()
                .map(this::buildItemFromActiboBolsa)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromActiboBolsa(ActivoBolsa activoBolsa) {
        return ItemBuilder.of(getState().getMaterial())
                .title(GOLD + "" + BOLD +  activoBolsa.getNombreLargo())
                .lore(List.of(
                        GOLD + "Ticker: " + activoBolsa.getNombreCorto(),
                        GOLD + "Precio: " + CARGANDO,
                        "",
                        activoBolsa.getActivoBolsaId().toString()
                ))
                .build();
    }

    private ItemStack itemNextPage() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "->").build();
    }

    private ItemStack itemBackPage() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "<-").build();
    }
}
