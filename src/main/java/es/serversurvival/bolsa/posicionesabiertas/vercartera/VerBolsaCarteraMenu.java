package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.menustate.AfterShow;
import es.jaimetruman.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class VerBolsaCarteraMenu extends Menu implements AfterShow {
    private static final String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + " TU CARTERA DE ACCIONES";

    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final Map<String, ActivoInfo> allActivosInfo;
    private final String jugadorNombre;
    private final MenuService menuService;

    private final double valorTotal;
    private double beneficiosOPerdidas;

    public VerBolsaCarteraMenu(String jugadorNombre){
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.menuService = DependecyContainer.get(MenuService.class);
        this.allActivosInfo = DependecyContainer.get(ActivosInfoService.class).findAllToMap();
        this.jugadorNombre = jugadorNombre;

        this.valorTotal = PosicionesUtils.getAllPixeloinsEnValores(jugadorNombre);
    }

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
                .title(TITULO)
                .fixedItems()
                .item(1, buildItemInfo())
                .item(5, buildItemStats())
                .items(2, buildItemsPosicionesAbiertas(), this::cerrarPosicion)
                .breakpoint(7, buildItemGoBackToProfileMenu(), this::goToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private ItemStack buildItemStats() {
        //We will build it later, in after show
        return ItemBuilder.of(Material.BOOK).title(GOLD + "" + BOLD + "STATS").build();
    }

    private void cerrarPosicion(Player player, InventoryClickEvent event) {
        UUID id = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 13));

        this.menuService.open(player, new BolsaCerrarPosicionMenu(id));
    }

    private List<ItemStack> buildItemsPosicionesAbiertas() {
        return this.posicionesAbiertasSerivce.findByJugador(this.jugadorNombre).stream()
                .map(this::buildItemFromPosicionAbierta)
                .toList();
    }

    private ItemStack buildItemFromPosicionAbierta(PosicionAbierta posicion) {
        var activoInfo = this.allActivosInfo.get(posicion.getNombreActivo());
        double perdidasOBeneficios = posicion.esLargo() ?
                posicion.getCantidad() * (activoInfo.getPrecio() - posicion.getPrecioApertura()) :
                posicion.getCantidad() * (posicion.getPrecioApertura() - activoInfo.getPrecio());
        double rentabildad = posicion.esLargo() ?
                redondeoDecimales(diferenciaPorcntual(posicion.getPrecioApertura(), activoInfo.getPrecio()), 2) :
                redondeoDecimales(diferenciaPorcntual(activoInfo.getPrecio(), posicion.getPrecioApertura()), 2);
        double valorPosicion = posicion.esLargo() ? posicion.getCantidad() * activoInfo.getPrecio() : perdidasOBeneficios;

        this.beneficiosOPerdidas += perdidasOBeneficios;

        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(GOLD + "Empresa " + activoInfo.getNombreActivoLargo());
        lore.add(GOLD + "Ticker " + posicion.getNombreActivo());
        double peso = valorTotal == 0 ? 0 : redondeoDecimales(valorPosicion / valorTotal, 0);
        lore.add(GOLD + "Peso " + FORMATEA.format(peso) + "%");
        lore.add("   ");
        lore.add(GOLD + "Cantidad: " + FORMATEA.format(posicion.getCantidad()) + " " + posicion.getTipoActivo().getAlias());
        lore.add(GOLD + "Precio apertura: " + GREEN + FORMATEA.format(posicion.getPrecioApertura()) + " PC");
        lore.add(GOLD + "Precio actual: " + GREEN + FORMATEA.format(activoInfo.getPrecio()) + " PC");
        lore.add(GOLD + "" + (rentabildad >= 0 ? GOLD +
                "Rentabilidad: " + GREEN + "+" + rentabildad + "%" :
                GOLD + "Rentabilidad: " + RED + rentabildad + "%"));
        lore.add(rentabildad >= 0 ?
                GOLD + "Beneficios totales: " + GREEN + "+" + FORMATEA.format(perdidasOBeneficios) + " PC" :
                GOLD + "Perdidas totales: " + RED + FORMATEA.format(perdidasOBeneficios) + " PC");

        lore.add(GOLD + "Valor total: " + GREEN + FORMATEA.format(activoInfo.getPrecio() * posicion.getCantidad()) + " PC");
        lore.add("   ");
        lore.add(GOLD + "Fecha de compra: " + posicion.getPrecioApertura());
        lore.add(String.valueOf(posicion.getPosicionAbiertaId()));

        return ItemBuilder.of(posicion.getMaterial())
                .title(GOLD + "" + BOLD + UNDERLINE + "CLICK PARA VENDER")
                .lore(lore)
                .build();
    }

    private ItemStack buildItemGoBackToProfileMenu() {
        return ItemBuilder.of(Material.RED_BANNER).title(ChatColor.RED + "Ir a perfil").build();
    }

    private void goToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new PerfilMenu(player.getName()));
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
    public void afterShow() {
        super.setItemLoreActualPage(8, List.of(
                GOLD + "Valor total: " + GREEN + FORMATEA.format(redondeoDecimales(valorTotal, 2)) + "PC",
                GOLD + "Resultado: " + (beneficiosOPerdidas >= 0 ? GREEN : RED) + FORMATEA.format(redondeoDecimales(beneficiosOPerdidas, 2)) + "PC",
                GOLD + "Rentabilidad: " + (valorTotal == 0 ? 0 : FORMATEA.format(redondeoDecimales(beneficiosOPerdidas/valorTotal, 0))) + "%"
        ));
    }
}
