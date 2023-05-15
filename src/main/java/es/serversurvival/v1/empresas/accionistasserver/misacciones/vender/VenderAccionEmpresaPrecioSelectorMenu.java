package es.serversurvival.v1.empresas.accionistasserver.misacciones.vender;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v1.empresas.ofertasaccionesserver.venderofertaaccionaserver.VenderOfertaAccionServerUseCase;
import es.serversurvival.v1.empresas.ofertasaccionesserver.verofertasaccioneserver.VerOfertasAccionesServerMenu;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderAccionEmpresaPrecioSelectorMenu extends NumberSelectorMenu<VenderAccionEmpresaPrecioSelectorMenuState> {
    private final VenderOfertaAccionServerUseCase venderOfertaUseCase;
    private final EnviadorMensajes enviadorMensajes;
    private final SyncMenuService syncMenuService;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, double precioVenta, InventoryClickEvent event) {
        venderOfertaUseCase.vender(player.getName(), this.getState().accionAVender().getAccionistaServerId(),
                precioVenta, this.getState().cantidadAVender());

        var menu = this.menuService.buildMenu(player, VerOfertasAccionesServerMenu.class);
        this.syncMenuService.sync(menu);

        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Al ser un accion de una empresa del servidor de minecraft. Se ha puesta " +
                        "la oferta de venta en el mercado de cantidad. Para consultar el mercado: " + AQUA + "/empresas mercado",
                Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha subido cantidad de la empresa del servidor: " +
                this.getState().accionAVender().getEmpresa() + AQUA + " /empresas mercado");
    }

    @Override
    public double maxValue() {
        return 999999;
    }

    @Override
    public double initialValue() {
        return this.getState().accionAVender().getPrecioApertura();
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "SELECCIONA PRECIO/ACCION";
    }

    @Override
    public List<String> loreItemAceptar(double precio) {
        return List.of(
                GOLD + "Vender " + this.getState().cantidadAVender() + " cantidad de " + this.getState().accionAVender().getEmpresa() + " a " + GREEN + FORMATEA.format(precio) + " PC",
                GOLD + "Compraste estas cantidad a " + GREEN + FORMATEA.format(this.getState().accionAVender().getPrecioApertura()) + " PC"
        );
    }
}
