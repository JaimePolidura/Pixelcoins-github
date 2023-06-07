package es.serversurvival.minecraftserver.bolsa.vervalores;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerActivosDisponiblesMenu extends Menu<TipoActivoBolsa> implements BeforeShow {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final TransaccionesService transaccionesService;
    private final ActivosBolsaService activosBolsaService;
    private final MenuService menuService;
    private final Executor executor;

    private List<ItemStack> itemsActivosMostrados;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
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
                .item(1, itemInfo())
                .items(2, itemValoresDisponibles(), this::onValorSeleccionado)
                .breakpoint(7)
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
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsaId);
        double pixelcoinsJugador = transaccionesService.getBalancePixelcions(player.getUniqueId());

        if(pixelcoinsJugador < ultimoPrecio){
            MinecraftUtils.enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        menuService.open(player, SeleccionarCantidadComprarBolsaMenu.class, SeleccionarCantidadComprarBolsaMenu.of(
                activoBolsa, ultimoPrecio, pixelcoinsJugador
        ));
    }

    private List<ItemStack> itemValoresDisponibles() {
        return activosBolsaService.findByTipo(getState()).stream()
                .map(this::buildItemFromActiboBolsa)
                .peek(itemsActivosMostrados::add)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromActiboBolsa(ActivoBolsa activoBolsa) {
        return ItemBuilder.of(getState().getMaterial())
                .title(GOLD + "" + BOLD +  activoBolsa.getNombreLargo())
                .lore(List.of(
                        GOLD + "Ticker: " + activoBolsa.getNombreCorto(),
                        GOLD + "Precio: " + RED + "Cargando...",
                        GOLD + "" + activoBolsa.getNReferencias() + " veces se ha comprado",
                        "",
                        activoBolsa.getActivoBolsaId().toString()
                ))
                .build();
    }

    @Override
    public void beforeShow(Player player) {
        executor.execute(() -> {
            for (ItemStack itemActivo : itemsActivosMostrados) {
                UUID activoId = MinecraftUtils.getLastLineOfLore(itemActivo, 0);
                double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoId);

                ItemUtils.setLore(itemActivo, 1, GOLD + "Precio: " + GREEN + Funciones.FORMATEA.format(ultimoPrecio)
                        + " PC / " + getState().getNombreUnidad());
            }
        });
    }

    private ItemStack itemInfo() {
        //TODO
        return null;
    }

    private ItemStack itemNextPage() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "->").build();
    }

    private ItemStack itemBackPage() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "<-").build();
    }
}
