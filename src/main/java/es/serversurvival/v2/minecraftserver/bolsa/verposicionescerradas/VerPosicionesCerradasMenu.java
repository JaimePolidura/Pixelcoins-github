package es.serversurvival.v2.minecraftserver.bolsa.verposicionescerradas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerPosicionesCerradasMenu extends Menu<VerPosicionesCerradasMenu.Orden> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final MenuService menuService;

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
                .breakpoint(7, buildItemGoBackToProfileMenu(), (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void cambiarOrden(Player player, InventoryClickEvent event) {
        Orden nuevoOrden = getState() == Orden.RENTABILIDAD ? Orden.FECHA : Orden.RENTABILIDAD;

        this.menuService.open(player, VerPosicionesCerradasMenu.class, nuevoOrden);
    }

    private ItemStack buildItemGoBackToProfileMenu() {
        return ItemBuilder.of(Material.RED_BANNER).title(ChatColor.RED + "Ir a perfil").build();
    }

    private List<ItemStack> buildItemPosicionesCerradas(Player player) {
        return posicionesService.findPosicionesCerradasByJugadorId(player.getUniqueId()).stream()
                .sorted(getState() == Orden.RENTABILIDAD ? Posicion.sortByFechaCierre() : Posicion.sortByRentabilidad())
                .map(this::buildItemFromPosicionCerrada)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromPosicionCerrada(Posicion posicion) {
        return ItemBuilder.of(posicion.getTipoApuesta().getMaterial())
                .title(activosBolsaService.getById(posicion.getActivoBolsaId()).getNombreLargo())
                .lore(List.of(
                        "Tipo apuesta: " + posicion.getTipoApuesta().toString().toLowerCase(),
                        "Precio apertura: " + GREEN + Funciones.FORMATEA.format(posicion.getPrecioApertura()) + " PC",
                        "Precio cierre: " + GREEN + Funciones.FORMATEA.format(posicion.getPrecioCierre()) + " PC",
                        "Rentabilidad: " + (posicion.getRentabilidad() >= 0 ? GREEN + "+" : RED) + posicion.getRentabilidad() + "%",
                        "Cantidad: " + posicion.getCantidad(),
                        "Precio actual: " + GREEN + Funciones.FORMATEA.format(activoBolsaUltimosPreciosService.getUltimoPrecio(posicion.getActivoBolsaId())) + " PC",
                        "Fecha apertura: " + posicion.getFechaApertura(),
                        "Fecha cierre: " + posicion.getFechaCierre()
                ))
                .build();
    }

    private ItemStack buidItemCambiarOrden() {
        return ItemBuilder.of(Material.HOPPER)
                .title(GOLD + "" + BOLD + "CLIK PARA ORDENDOR POR" + (getState() == Orden.RENTABILIDAD ? "RENTABILIDAD" : "FECHA"))
                .build();
    }

    private ItemStack buildItemInfo() {
        //TODO
        return null;
    }

    public enum Orden {
        FECHA, RENTABILIDAD
    }
}
