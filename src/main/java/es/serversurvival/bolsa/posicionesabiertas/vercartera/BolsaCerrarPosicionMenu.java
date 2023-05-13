package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand.of;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class BolsaCerrarPosicionMenu extends NumberSelectorMenu<BolsaCerrarPosicionMenuState> {
    private final ComprarCortoUseCase comprarCortoUseCase;
    private final OrderExecutorProxy orderExecutorProxy;
    private final VenderLargoUseCase venderLargoUseCase;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        var executedInMarket = this.orderExecutorProxy.execute(of(
                player.getName(),
                getState().posicion().getNombreActivo(),
                (int) cantidad,
                getState().posicion().esLargo() ? TipoAccion.LARGO_VENTA : TipoAccion.CORTO_COMPRA,
                getState().posicion().getPosicionAbiertaId()), () -> {
                    if(getState().posicion().esLargo())
                        venderLargoUseCase.venderPosicion(getState().posicion().getPosicionAbiertaId(), (int) cantidad, player.getName());
                    else
                        comprarCortoUseCase.comprarPosicionCorto(getState().posicion().getPosicionAbiertaId(), (int) cantidad, getState().posicion().getJugador());
                }
        );

        double resultadoNeto = getState().posicion().getCantidad() * (getState().posicion().esLargo() ?
                getState().activoInfo().getPrecio() - getState().posicion().getPrecioApertura() :
                getState().posicion().getPrecioApertura() - getState().activoInfo().getPrecio());

        if(executedInMarket){
            String mensajeResultado  = resultadoNeto >= 0 ?
                    "unos beneficios de " + GREEN + "+ " + FORMATEA.format(resultadoNeto) + "PC" :
                    "unas perdidas de " + RED + FORMATEA.format(resultadoNeto) + "PC";

            player.sendMessage(GOLD + "Has cerra de la posicion de " + cantidad + " cantidad de " + getState().activoInfo().getNombreActivoLargo() + " por " + GREEN +
                    FORMATEA.format(getState().activoInfo().getPrecio()) + "PC " + GOLD + "Con " + mensajeResultado);
            player.playSound(player.getLocation(), resultadoNeto >= 0 ? Sound.ENTITY_PLAYER_LEVELUP : Sound.BLOCK_ANVIL_LAND, 10, 1);
        }else{
            player.sendMessage(GOLD + "No se ha podido cerrar la posicion por que el mercado esta cerrado. Cuando abra se ejecutara y recibiras tus pixelcoins");
        }

    }

    @Override
    public void onCancel(Player player, InventoryClickEvent event) {
        this.menuService.open(player, (Class<? extends Menu<?>>) VerBolsaCarteraMenu.class);
    }

    @Override
    public double maxValue() {
        return getState().posicion().getCantidad();
    }

    @Override
    public double initialValue() {
        return getState().posicion().getCantidad();
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        int cantidadInt = (int) cantidad;

        return List.of(
                GOLD + "Cantidad: " + FORMATEA.format(cantidadInt),
                GOLD + "Valor total: " + GREEN + FORMATEA.format(redondeoDecimales(calcularValorTotal(cantidadInt), 2)) + " PC",
                calcularResultado(cantidadInt) >= 0?
                        GOLD + "Resultado: " + GREEN + "+" + FORMATEA.format(redondeoDecimales(calcularResultado(cantidadInt), 2)) + "PC" :
                        GOLD + "Resultado: " + RED + FORMATEA.format(redondeoDecimales(calcularResultado(cantidadInt), 2)) + "PC"
        );
    }

    private double calcularValorTotal(int cantidadACerrar){
        return getState().posicion().esCorto() ?
                (getState().posicion().getPrecioApertura() - getState().activoInfo().getPrecio()) * cantidadACerrar :
                cantidadACerrar * getState().activoInfo().getPrecio();
    }

    private double calcularResultado(int cantidadACerrar){
        return getState().posicion().esCorto() ?
                (getState().posicion().getPrecioApertura() - getState().activoInfo().getPrecio()) * cantidadACerrar :
                (getState().activoInfo().getPrecio() - getState().posicion().getPrecioApertura()) * cantidadACerrar;
    }
}
