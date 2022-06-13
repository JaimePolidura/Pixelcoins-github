package es.serversurvival.empresas.ofertasaccionesserver;

import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver.VenderOfertaAccionServerUseCase;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.DATE_FORMATER_LEGACY;
import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class VenderAccionEmpresa extends NumberSelectorMenu {
    private final VenderOfertaAccionServerUseCase venderOfertaUseCase;

    private final AccionistaServer accionEmpresaAVender;
    private final String empresa;
    private final double precioApertura;

    public VenderAccionEmpresa(AccionistaServer accionEmpresaAVender) {
        this.accionEmpresaAVender = accionEmpresaAVender;
        this.venderOfertaUseCase = new VenderOfertaAccionServerUseCase();

        this.empresa = accionEmpresaAVender.getEmpresa();
        this.precioApertura = accionEmpresaAVender.getPrecioApertura();
    }

    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        double precioVenta = super.getPropertyDouble("cantidad");
        venderOfertaUseCase.vender(player.getName(),this.accionEmpresaAVender.getAccionistaServerId(),
                precioVenta, this.accionEmpresaAVender.getCantidad());

        enviarMensajeYSonido(player, GOLD + "Al ser un accion de una empresa del servidor de minecraft. Se ha puesta la oferta de venta en el mercado de acciones. Para consultar el mercado: " + AQUA + "/empresas mercado",
                Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(GOLD + player.getName() + " ha subido acciones de la empresa del servidor: " + accionEmpresaAVender.getEmpresa() + AQUA + " /empresas mercado");
    }

    @Override
    public double maxValue() {
        return 999999;
    }

    @Override
    public double initialValue() {
        return this.accionEmpresaAVender.getPrecioApertura();
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Vender 1 accion de " + empresa + " a " + GREEN + DATE_FORMATER_LEGACY.format(cantidad) + " PC",
                GOLD + "Compraste estas acciones a " + GREEN + DATE_FORMATER_LEGACY.format(precioApertura) + " PC"
        );
    }
}
