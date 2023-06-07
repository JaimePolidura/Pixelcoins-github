package es.serversurvival.minecraftserver.empresas.misacciones;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival.minecraftserver._shared.menus.NumberSelectorMenu;
import es.serversurvival.minecraftserver.empresas.mercado.MercadoAccionesEmpresasMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas.ponerventa.PonerVentaAccionesParametros;
import es.serversurvival.pixelcoins.empresas.ponerventa.PonerVentaAccionesUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderAccionEmpresaPrecioSelectorMenu extends NumberSelectorMenu<VenderAccionEmpresaPrecioSelectorMenu.VenderAccionEmpresaPrecioSelectorMenuState> {
    private final PonerVentaAccionesUseCase ponerVentaAccionesUseCase;
    private final SyncMenuService syncMenuService;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, double precio, InventoryClickEvent event) {
        ponerVentaAccionesUseCase.ponerVenta(PonerVentaAccionesParametros.builder()
                        .cantidadAcciones(getState().cantidadAVender)
                        .empresaId(getState().empresa().getEmpresaId())
                        .precioPorAccion(precio)
                        .jugadorId(player.getUniqueId())
                .build());

        var menu = this.menuService.buildMenu(player, MercadoAccionesEmpresasMenu.class);
        this.syncMenuService.sync(menu);

        enviarMensajeYSonido(player, GOLD + "Al ser un accion de una empresa del servidor de minecraft. Se ha puesta " +
                        "la oferta de venta en el mercado de cantidad. Para consultar el mercado: " + AQUA + "/empresas mercado",
                Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha subido cantidad de la empresa del servidor: " +
                this.getState().empresa.getNombre() + AQUA + " /empresas mercado");
    }

    @Override
    public double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public double initialValue() {
        return 10;
    }

    @Override
    protected String titulo() {
        return DARK_RED + "" + BOLD + "SELECCIONA PRECIO/ACCION";
    }

    @Override
    public List<String> loreItemAceptar(double precio) {
        return List.of(
                GOLD + "Vender " + this.getState().cantidadAVender() + " cantidad de " + this.getState().empresa.getNombre() + " a " + GREEN + FORMATEA.format(precio) + " PC"
        );
    }

    public static VenderAccionEmpresaPrecioSelectorMenuState of(int cantidadAVender, AccionistaEmpresa accionAVender, Empresa empresa) {
        return new VenderAccionEmpresaPrecioSelectorMenuState(cantidadAVender, accionAVender, empresa);
    }

    public record VenderAccionEmpresaPrecioSelectorMenuState(int cantidadAVender, AccionistaEmpresa accionista, Empresa empresa) { }
}
