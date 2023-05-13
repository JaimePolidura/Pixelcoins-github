package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver.VerOfertasAccionesServerMenu;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ComprarAccionesServerConfirmacion extends NumberSelectorMenu<OfertaAccionServer> implements BeforeShow {
    private final ComprarOfertaMercadoUseCase comprarOfertaMercadoUseCase;
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final SyncMenuService syncMenuService;
    private final MenuService menuService;

    private Jugador jugador;

    @Override
    public void onAccept(Player player, double cantidad, InventoryClickEvent event) {
        comprarOfertaMercadoUseCase.comprarOfertaMercadoAccionServer(player.getName(),
                getState().getOfertaAccionServerId(), (int) cantidad);

        var newPages = this.menuService.buildPages(player, VerOfertasAccionesServerMenu.class);
        syncMenuService.sync(VerOfertasAccionesServerMenu.class, newPages, this.getConfiguration().getSyncMenuConfiguration());

        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Has comprado " + FORMATEA.format(cantidad) + " cantidad a " + GREEN +
                FORMATEA.format(getState().getPrecio()) + " PC" + GOLD + " que es un total de " + GREEN + FORMATEA.format(
                cantidad * getState().getPrecio()) + " PC " + GOLD + " comandos: " + AQUA +
                "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidad + " cantidad de la empresa del server: "
                + getState().getEmpresa() + " a " + GREEN + FORMATEA.format(getState().getPrecio()) + "PC");
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " cantidad de " + this.getState().getEmpresa(),
                GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(getState().getPrecio()) + " PC",
                GOLD + "Total: " + GREEN + FORMATEA.format(getState().getPrecio() * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + FORMATEA.format(this.jugador.getPixelcoins()) + " PC"
        );
    }

    @Override
    public double maxValue() {
        return this.jugador.getPixelcoins() >= (getState().getCantidad() * getState().getPrecio()) ?
                getState().getCantidad() :
                jugador.getPixelcoins() / getState().getPrecio();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public void beforeShow(Player player) {
        this.jugador = this.jugadoresService.getByNombre(player.getName());
    }
}
