package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.bolsa.verordenespremarket.MisOrdenesPremarketMenu;
import es.serversurvival.minecraftserver.bolsa.verposicionescerradas.VerPosicionesCerradasMenu;
import es.serversurvival.minecraftserver.bolsa.vervalores.ElegirTipoActivoDisponibleMenu;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.PosicionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CARGANDO;
import static es.serversurvival.minecraftserver._shared.menus.MenuItems.CLICKEABLE;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class MiCarteraBolsaMenu extends Menu implements AfterShow {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final MenuService menuService;
    private final Executor executor;

    private Map<UUID, Posicion> posicionesJugadorById = new HashMap<>();
    private Set<UUID> activosIdYaVistos = new HashSet<>();

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 3, 0, 0, 0, 0, 0, 5 },
                {6, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + " TU CARTERA DE ACCIONES")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(2, buildItemEligerInversion(), (p, e) -> menuService.open(p, ElegirTipoActivoDisponibleMenu.class))
                .item(3, buildItemPosicionesCerradas(), (p, e) -> menuService.open(p, VerPosicionesCerradasMenu.class))
                .item(4, buildItemOrdenesPremarket(), (p, e) -> menuService.open(p, MisOrdenesPremarketMenu.class))
                .item(5, buildItemStats())
                .items(6, this::buildItemsPosicionesAbiertas, this::cerrarPosicion)
                .breakpoint(7, buildItemGoBackToProfileMenu(), this::goToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private ItemStack buildItemOrdenesPremarket() {
        return ItemBuilder.of(Material.REPEATER).title(CLICKEABLE + "VER ORDENES PREMARKET").build();
    }

    private ItemStack buildItemEligerInversion() {
        return ItemBuilder.of(Material.ENDER_EYE).title(CLICKEABLE + "ELEGIR INVERSION").build();
    }

    private ItemStack buildItemPosicionesCerradas() {
        return ItemBuilder.of(Material.IRON_DOOR).title(CLICKEABLE + "VER POSICIONES CERRADAS").build();
    }

    //Lo actualizaremos cuando se ejecute aftershow
    private ItemStack buildItemStats() {
        return ItemBuilder.of(Material.BOOK).title(GOLD + "" + BOLD + "RESUMEN").build();
    }

    private void cerrarPosicion(Player player, InventoryClickEvent event) {
        UUID posicionId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
        Posicion posicion = posicionesService.getById(posicionId);

        this.menuService.open(player, BolsaCerrarPosicionMenu.class, posicion);
    }

    private List<ItemStack> buildItemsPosicionesAbiertas(Player player) {
        return this.posicionesService.findPosicionesAbiertasByJugadorId(player.getUniqueId()).stream()
                .map(this::buildItemFromPosicionAbierta)
                .toList();
    }

    private ItemStack buildItemFromPosicionAbierta(Posicion posicion) {
        ActivoBolsa activoBolsa = activosBolsaService.getById(posicion.getActivoBolsaId());
        posicionesJugadorById.put(posicion.getPosicionId(), posicion);

        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empresa " + activoBolsa.getNombreLargo());
        lore.add(GOLD + "Ticker " + activoBolsa.getNombreCorto());
        lore.add("   ");
        lore.add(GOLD + "Cantidad: " + formatNumero(posicion.getCantidad()) + " " + activoBolsa.getTipoActivo().getNombreUnidadPlural());
        lore.add(GOLD + "Precio apertura: " + formatPixelcoins(posicion.getPrecioApertura()));
        lore.add(GOLD + "Precio actual: " + CARGANDO);
        lore.add(GOLD + "Rentabilidad: " + CARGANDO);

        lore.add(GOLD + "Valor total: " + CARGANDO);
        lore.add(GOLD + "Fecha de compra: " + posicion.getPrecioApertura());
        lore.add("   ");
        lore.add(posicion.getPosicionId().toString());

        return ItemBuilder.of(activoBolsa.getTipoActivo() == TipoActivoBolsa.ACCION ?
                        posicion.getTipoApuesta().getMaterial() :
                        activoBolsa.getTipoActivo().getMaterial())
                .title(GOLD + "" + BOLD + UNDERLINE + "CERRAR POSICION")
                .lore(lore)
                .build();
    }

    private ItemStack buildItemGoBackToProfileMenu() {
        return ItemBuilder.of(Material.RED_BANNER).title(ChatColor.RED + "Ir a perfil").build();
    }

    private void goToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Puedes comprar valores en la bolsa",
                        "que cotizen en Estados Unidos",
                        "Para buscar valores para invertir",
                        "le das click al item de la derecha",
                        "Si quieres otro valor que no este",
                        "en la lista debes poner:",
                        "/bolsa invertir <ticker> <nacciones>",
                        "  <ticker> es la letra identificatoria",
                        "    del valor: ejemplo Amazon: AMZN",
                        "    (la empresa debe cotizar en USA)",
                        "   <nacciones> numero de cantidad a comprar",
                        "   ",
                        "Para consultar tus valores en carteras ",
                        "y venderlas tienes tres vias: el menu actual,",
                        "la web y en el comando /bolsa cartera",
                        "   ",
                        "Mas info en /bolsa ayuda o /ayuda bolsa o en:",
                        "http://serversurvival2.ddns.net/perfil"
                ))
                .build();
    }

    @Override
    public void afterShow(Player player) {
        executor.execute(() -> {
            List<ItemStack> itemPosiciones = super.getAllItemsByItemNum(6);
            double valorInicialInvertidoTotalCartera = 0.0d;
            double beneficiosOPerdidasTotalCartera = 0.0d;
            double valorTotalCartera = 0.0d;

            for (int i = 0; i < itemPosiciones.size(); i++) {
                ItemStack itemPosicion = itemPosiciones.get(i);
                UUID posicionId = MinecraftUtils.getLastLineOfLore(itemPosicion, 0);
                Posicion posicion = posicionesJugadorById.get(posicionId);
                boolean activoIdYaVisto = activosIdYaVistos.contains(posicion.getActivoBolsaId());
                int slot = i + 9;

                TipoApuestaService tipoApuestaService = dependenciesRepository.get(posicion.getTipoApuesta().getTipoApuestaService());
                double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(posicion.getActivoBolsaId(), activoIdYaVisto ? null : player.getUniqueId());
                double rentabilidad = tipoApuestaService.calcularRentabilidad(posicion.getPrecioApertura(), ultimoPrecio);
                double valorTotalPosicion = tipoApuestaService.getPixelcoinsCerrarPosicion(posicion.getPosicionId(), posicion.getCantidad(), ultimoPrecio);
                double beneficiosOPerdidasPosicion = tipoApuestaService.calcularBeneficiosOPerdidas(posicion.getPrecioApertura(), ultimoPrecio, posicion.getCantidad());

                valorInicialInvertidoTotalCartera += ultimoPrecio * posicion.getPrecioApertura();
                beneficiosOPerdidasTotalCartera += beneficiosOPerdidasPosicion;
                valorTotalCartera += valorTotalPosicion;

                super.setItemLore(slot, 6, GOLD + "Precio actual: " + formatPixelcoins(ultimoPrecio));
                super.setItemLore(slot, 7, GOLD + "Rentabilidad: " + formatRentabilidad(rentabilidad));
                super.setItemLore(slot, 8, GOLD + (beneficiosOPerdidasPosicion >= 0 ? "Beneficios: " : "Perdidas: ") + formatPixelcoins(beneficiosOPerdidasPosicion));
                super.setItemLore(slot, 9, GOLD + "Valor total: " + formatPixelcoins(valorTotalPosicion));
            }

            super.setItemLoreActualPage(8, List.of(
                    GOLD + "Valor total: " + formatPixelcoins(valorTotalCartera),
                    GOLD + "Resultado: " + formatPixelcoins(beneficiosOPerdidasTotalCartera),
                    GOLD + "Rentabilidad: " + formatRentabilidad(valorTotalCartera == 0 ? 0 : beneficiosOPerdidasTotalCartera / valorInicialInvertidoTotalCartera)
            ));
        });
    }
}
