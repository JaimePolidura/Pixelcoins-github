package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.confirmation.ConfirmationConfiguration;
import es.jaimetruman.menus.modules.numberselector.NumberSelectorMenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.DECREASE;
import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.INCREASE;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand.of;
import static org.bukkit.ChatColor.*;

public final class BolsaCerrarPosicionMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD  + "   CERRAR POSICION";

    private final PosicionAbierta posicion;
    private final ActivoInfo activoInfoAVender;
    private final MenuService menuService;
    private final VenderLargoUseCase venderLargoUseCase;
    private final ComprarCortoUseCase comprarCortoUseCase;

    public BolsaCerrarPosicionMenu(UUID posicionAbiertaId) {
        this.venderLargoUseCase = new VenderLargoUseCase();
        this.comprarCortoUseCase = new ComprarCortoUseCase();
        this.menuService = DependecyContainer.get(MenuService.class);
        this.posicion = DependecyContainer.get(PosicionesAbiertasSerivce.class).getById(posicionAbiertaId);
        this.activoInfoAVender = DependecyContainer.get(ActivosInfoService.class).getByNombreActivo(posicion.getNombreActivo(),
                posicion.getTipoActivo());
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0  },
                {1, 2, 3, 4, 0, 5, 6, 7, 8  },
                {0, 0, 0, 0, 0, 0, 0, 0, 0  }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(5, buildItemCancel(), this::onCancel)
                        .accept(5, buildItemAccept(posicion.getCantidad()), this::onAccept)
                        .build())
                .numberSelector(NumberSelectorMenuConfiguration.builder()
                        .initialValue(posicion.getCantidad())
                        .minValue(1)
                        .maxValue(posicion.getCantidad())
                        .valuePropertyName("cantidad")
                        .onValueChanged(this::onCantidadChangedChangeItemAcetpar)
                        .item(1, DECREASE, 1, buildItemNumberSelector(-1))
                        .item(2, DECREASE, 5, buildItemNumberSelector(-5))
                        .item(3, DECREASE, 10, buildItemNumberSelector(-10))
                        .item(6, INCREASE, 1, buildItemNumberSelector(1))
                        .item(7, INCREASE, 5, buildItemNumberSelector(5))
                        .item(8, INCREASE, 10, buildItemNumberSelector(10))
                        .build())
                .build();
    }

    private void onCantidadChangedChangeItemAcetpar(double nuevaCantidad) {
        super.setItem(14, buildItemAccept((int) nuevaCantidad));
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        OrderExecutorProxy.execute(of(
                        player.getName(),
                        posicion.getNombreActivo(),
                        posicion.getCantidad(),
                        posicion.esLargo() ? TipoAccion.LARGO_VENTA : TipoAccion.CORTO_COMPRA,
                        posicion.getPosicionAbiertaId()
                ), () -> {
                    if(posicion.esLargo())
                        venderLargoUseCase.venderPosicion(posicion.getPosicionAbiertaId(), posicion.getCantidad(), player.getName());
                    else
                        comprarCortoUseCase.comprarPosicionCorto(posicion.getPosicionAbiertaId(), posicion.getCantidad(), posicion.getJugador());
                }
        );
    }

    private void onCancel(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new VerBolsaCarteraMenu(player.getName()));
    }

    private ItemStack buildItemAccept(int cantidadACerrar) {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "CERRAR POSICION")
                .lore(List.of(
                        GOLD + "Cantidad: " + FORMATEA.format(cantidadACerrar),
                        GOLD + "Valor total: " + GREEN + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadACerrar), 2)),
                        calcularResultado(cantidadACerrar) >= 0?
                                GOLD + "Resuldao: " + GREEN + "+" + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadACerrar), 2)) :
                                GOLD + "Resuldao: " + RED + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadACerrar), 2))
                ))
                .build();
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL)
                .title(RED + "" + BOLD + "Cancelar")
                .build();
    }

    private ItemStack buildItemNumberSelector(int i) {
        Material material = i < 0 ? Material.RED_BANNER : Material.GREEN_BANNER;
        String title = i < 0 ? RED + "" + BOLD + i : GREEN + "" + BOLD + "+" + i;

        return ItemBuilder.of(material).title(title).build();
    }

    private double calcularValorTotal(int cantidadACerrar){
        return posicion.esCorto() ?
                (posicion.getPrecioApertura() - activoInfoAVender.getPrecio()) * cantidadACerrar :
                cantidadACerrar * activoInfoAVender.getPrecio();
    }

    private double calcularResultado(int cantidadACerrar){
        return posicion.esCorto() ?
                (posicion.getPrecioApertura() - activoInfoAVender.getPrecio()) * cantidadACerrar :
                (activoInfoAVender.getPrecio() - posicion.getPrecioApertura()) * cantidadACerrar;
    }
}
