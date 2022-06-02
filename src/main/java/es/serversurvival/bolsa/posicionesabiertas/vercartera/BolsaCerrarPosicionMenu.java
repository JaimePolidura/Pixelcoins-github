package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand.of;
import static org.bukkit.ChatColor.*;

public final class BolsaCerrarPosicionMenu extends NumberSelectorMenu {
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
    public void onAccept(Player player, InventoryClickEvent event) {
        int cantidad = (int) super.getPropertyDouble("cantidad");

        OrderExecutorProxy.execute(of(
                player.getName(),
                posicion.getNombreActivo(),
                cantidad,
                posicion.esLargo() ? TipoAccion.LARGO_VENTA : TipoAccion.CORTO_COMPRA,
                posicion.getPosicionAbiertaId()), () -> {
                    if(posicion.esLargo())
                        venderLargoUseCase.venderPosicion(posicion.getPosicionAbiertaId(), cantidad, player.getName());
                    else
                        comprarCortoUseCase.comprarPosicionCorto(posicion.getPosicionAbiertaId(), cantidad, posicion.getJugador());
                }
        );
    }

    @Override
    public void onCancel(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new VerBolsaCarteraMenu(player.getName()));
    }

    @Override
    public double maxValue() {
        return posicion.getCantidad();
    }

    @Override
    public double initialValue() {
        return posicion.getCantidad();
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        int cantidadInt = (int) cantidad;

        return List.of(
                GOLD + "Cantidad: " + FORMATEA.format(cantidadInt),
                GOLD + "Valor total: " + GREEN + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadInt), 2)),
                calcularResultado(cantidadInt) >= 0?
                        GOLD + "Resuldao: " + GREEN + "+" + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadInt), 2)) :
                        GOLD + "Resuldao: " + RED + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadInt), 2))
        );
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
