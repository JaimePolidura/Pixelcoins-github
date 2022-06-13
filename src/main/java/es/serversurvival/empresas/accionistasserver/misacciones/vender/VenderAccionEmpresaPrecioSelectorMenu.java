package es.serversurvival.empresas.accionistasserver.misacciones.vender;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver.VenderOfertaAccionServerUseCase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

public final class VenderAccionEmpresaPrecioSelectorMenu extends NumberSelectorMenu {
    private final int cantidadAVender;
    private final AccionistaServer accionAVender;
    private final VenderOfertaAccionServerUseCase venderOfertaUseCase;

    public VenderAccionEmpresaPrecioSelectorMenu(int cantidadAVender, UUID accionAVenderId) {
        this.cantidadAVender = cantidadAVender;
        this.accionAVender = DependecyContainer.get(AccionistasServerService.class).getById(accionAVenderId);
        this.venderOfertaUseCase = new VenderOfertaAccionServerUseCase();
    }

    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        double precioVenta = super.getPropertyDouble("cantidad");
        venderOfertaUseCase.vender(player.getName(), this.accionAVender.getAccionistaServerId(),
                precioVenta, this.cantidadAVender);

        enviarMensajeYSonido(player, GOLD + "Al ser un accion de una empresa del servidor de minecraft. Se ha puesta " +
                        "la oferta de venta en el mercado de acciones. Para consultar el mercado: " + AQUA + "/empresas mercado",
                Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha subido acciones de la empresa del servidor: " +
                accionAVender.getEmpresa() + AQUA + " /empresas mercado");
    }

    @Override
    public double maxValue() {
        return 999999;
    }

    @Override
    public double initialValue() {
        return this.accionAVender.getPrecioApertura();
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "SELECCIONA PRECIO/ACCION";
    }

    @Override
    public List<String> loreItemAceptar(double precio) {
        return List.of(
                GOLD + "Vender " + this.cantidadAVender + " acciones de " + this.accionAVender.getEmpresa() + " a " + GREEN + FORMATEA.format(precio) + " PC",
                GOLD + "Compraste estas acciones a " + GREEN + FORMATEA.format(this.accionAVender.getPrecioApertura()) + " PC"
        );
    }
}
