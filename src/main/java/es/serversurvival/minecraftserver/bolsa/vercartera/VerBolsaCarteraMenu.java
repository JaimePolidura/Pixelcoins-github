package es.serversurvival.minecraftserver.bolsa.vercartera;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.PosicionesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerBolsaCarteraMenu extends Menu implements AfterShow {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;
    private final PosicionesService posicionesService;
    private final MenuService menuService;

    private double valorInicialInvertidoTotalCartera;
    private double beneficiosOPerdidasTotalCartera;
    private double valorTotalCartera;

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
                .title(ChatColor.DARK_RED + "" + ChatColor.BOLD + " TU CARTERA DE ACCIONES")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(5, buildItemStats())
                .items(2, this::buildItemsPosicionesAbiertas, this::cerrarPosicion)
                .breakpoint(7, buildItemGoBackToProfileMenu(), this::goToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    //Lo actualizaremos cuando se ejecute aftershow
    private ItemStack buildItemStats() {
        return ItemBuilder.of(Material.BOOK).title(GOLD + "" + BOLD + "STATS").build();
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
        TipoApuestaService tipoApuestaService = dependenciesRepository.get(posicion.getTipoApuesta().getTipoApuestaService());
        ActivoBolsa activoBolsa = activosBolsaService.getById(posicion.getActivoBolsaId());

        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(posicion.getActivoBolsaId());
        double rentabilidad = tipoApuestaService.calcularRentabilidad(posicion.getPrecioApertura(), posicion.getPrecioCierre());
        double valorTotalPosicion = tipoApuestaService.getPixelcoinsCerrarPosicion(posicion.getPosicionId(), posicion.getCantidad(), ultimoPrecio);
        double beneficiosOPerdidasPosicion = tipoApuestaService.calcularBeneficiosOPerdidas(posicion.getPrecioApertura(), posicion.getPrecioCierre(), posicion.getCantidad());

        valorInicialInvertidoTotalCartera += ultimoPrecio * posicion.getPrecioApertura();
        beneficiosOPerdidasTotalCartera += beneficiosOPerdidasPosicion;
        valorTotalCartera += valorTotalPosicion;

        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empresa " + activoBolsa.getNombreLargo());
        lore.add(GOLD + "Ticker " + activoBolsa.getNombreCorto());
        lore.add("   ");
        lore.add(GOLD + "Cantidad: " + FORMATEA.format(posicion.getCantidad()) + " " + activoBolsa.getTipoActivoBolsa().getNombreUnidad());
        lore.add(GOLD + "Precio apertura: " + GREEN + FORMATEA.format(posicion.getPrecioApertura()) + " PC");
        lore.add(GOLD + "Precio actual: " + GREEN + FORMATEA.format(ultimoPrecio) + " PC");
        lore.add(GOLD + "Rentabilidad: " + (rentabilidad >= 0 ? GREEN + "+" + rentabilidad : RED + "" +rentabilidad) + "%");
        lore.add(beneficiosOPerdidasPosicion >= 0 ?
                GOLD + "Beneficios: " + GREEN + "+" + FORMATEA.format(beneficiosOPerdidasPosicion) + " PC" :
                GOLD + "Perdidas: " + RED + FORMATEA.format(beneficiosOPerdidasPosicion) + " PC");

        lore.add(GOLD + "Valor total: " + GREEN + FORMATEA.format(valorTotalPosicion) + " PC");
        lore.add(GOLD + "Fecha de compra: " + posicion.getPrecioApertura());
        lore.add("   ");
        lore.add(posicion.getPosicionId().toString());
        
        return ItemBuilder.of(posicion.getTipoApuesta().getMaterial())
                .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA CERRAR")
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
        super.setItemLoreActualPage(8, List.of(
                GOLD + "Valor total: " + GREEN + FORMATEA.format(redondeoDecimales(valorTotalCartera, 2)) + "PC",
                GOLD + "Resultado: " + (beneficiosOPerdidasTotalCartera >= 0 ? GREEN : RED) + FORMATEA.format(redondeoDecimales(beneficiosOPerdidasTotalCartera, 2)) + "PC",
                GOLD + "Rentabilidad: " + (valorTotalCartera == 0 ? 0 : FORMATEA.format(redondeoDecimales(beneficiosOPerdidasTotalCartera /valorInicialInvertidoTotalCartera, 0))) + "%"
        ));
    }
}
