package es.serversurvival.minecraftserver.bolsa.verposicionescerradas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.Page;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.async.config.AsyncTasksConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CARGANDO;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CLICKEABLE;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerPosicionesCerradasMenu extends Menu<VerPosicionesCerradasMenu.Orden> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final MenuService menuService;

    private Map<UUID, Posicion> posicionesCerradasJugadorById = new HashMap<>();
    private Set<UUID> activosIdYaVistos = new HashSet<>();

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 5 },
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
                .title(DARK_RED + "" + BOLD + " TUS POSICIONES CERRADAS")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(5, buidItemCambiarOrden(), this::cambiarOrden)
                .items(2, this::buildItemPosicionesCerradas)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, PerfilMenu.class))
                .asyncTasks(AsyncTasksConfiguration.builder()
                        .onPageLoaded(2, this::mostrarPrecioActualEnBolsa)
                        .build())
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void mostrarPrecioActualEnBolsa(Page page, Integer slot, ItemStack itemPosicion) {
        UUID posicionId = MinecraftUtils.getLastLineOfLore(itemPosicion, 0);
        Posicion posicion = posicionesCerradasJugadorById.get(posicionId);
        boolean activoIdYaVisto = activosIdYaVistos.contains(posicion.getActivoBolsaId());

        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(posicion.getActivoBolsaId(), activoIdYaVisto ? null : posicion.getJugadorId());

        super.setItemLore(page.getPageId(), slot, 5, GOLD + "Precio actual: " + formatPixelcoins(ultimoPrecio));

        activosIdYaVistos.add(posicion.getPosicionId());
    }

    private void cambiarOrden(Player player, InventoryClickEvent event) {
        Orden nuevoOrden = getState() == Orden.RENTABILIDAD ? Orden.FECHA : Orden.RENTABILIDAD;

        this.menuService.open(player, VerPosicionesCerradasMenu.class, nuevoOrden);
    }

    private List<ItemStack> buildItemPosicionesCerradas(Player player) {
        return posicionesService.findPosicionesCerradasByJugadorId(player.getUniqueId()).stream()
                .sorted(getState() == Orden.RENTABILIDAD ? Posicion.sortByFechaCierre() : Posicion.sortByRentabilidad())
                .map(this::buildItemFromPosicionCerrada)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromPosicionCerrada(Posicion posicion) {
        posicionesCerradasJugadorById.put(posicion.getPosicionId(), posicion);

        return ItemBuilder.of(posicion.getTipoApuesta().getMaterial())
                .title(GOLD + "" + BOLD + activosBolsaService.getById(posicion.getActivoBolsaId()).getNombreLargo())
                .lore(List.of(
                        GOLD + "Tipo apuesta: " + posicion.getTipoApuesta().toString().toLowerCase(),
                        GOLD + "Precio apertura: " + formatPixelcoins(posicion.getPrecioApertura()),
                        GOLD + "Precio cierre: " + formatPixelcoins(posicion.getPrecioCierre()),
                        GOLD + "Rentabilidad: " + formatRentabilidad(posicion.getRentabilidad()),
                        GOLD + "Cantidad: " + posicion.getCantidad(),
                        GOLD + "Precio actual: " + CARGANDO,
                        GOLD + "Fecha apertura: " + Funciones.toString(posicion.getFechaApertura()),
                        GOLD + "Fecha cierre: " + Funciones.toString(posicion.getFechaCierre()),
                        "",
                        posicion.getPosicionId().toString()
                ))
                .build();
    }

    private ItemStack buidItemCambiarOrden() {
        return ItemBuilder.of(Material.HOPPER)
                .title(CLICKEABLE + "ORDENAR POR " + (getState() == Orden.RENTABILIDAD ? "RENTABILIDAD" : "FECHA"))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of(
                        GOLD + "Las posiciones cerradas son las ventas que has hecho",
                        GOLD + "en bolsa de criptomonedas y acciones."
                ))
                .build();
    }

    public enum Orden {
        FECHA, RENTABILIDAD
    }
}
