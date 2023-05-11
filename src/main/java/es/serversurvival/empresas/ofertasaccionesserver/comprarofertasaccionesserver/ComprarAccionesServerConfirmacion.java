package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver.VerOfertasAccionesServerMenu;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ComprarAccionesServerConfirmacion extends NumberSelectorMenu<OfertaAccionServer> {
    private final SyncMenuService syncMenuService;
    private final Jugador compradorJugador;
    private final MenuService menuService;

    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        int cantidadToComprar = (int) super.getPropertyDouble("cantidad");

        new ComprarOfertaMercadoUseCase().comprarOfertaMercadoAccionServer(player.getName(),
                getState().getOfertaAccionServerId(), cantidadToComprar);

        var newPages = this.menuService.buildPages(new VerOfertasAccionesServerMenu(player));
        this.syncMenuService.sync(VerOfertasAccionesServerMenu.class, newPages);

        enviarMensajeYSonido(player, GOLD + "Has comprado " + FORMATEA.format(cantidadToComprar) + " cantidad a " + GREEN +
                FORMATEA.format(getState().getPrecio()) + " PC" + GOLD + " que es un total de " + GREEN + FORMATEA.format(
                cantidadToComprar * getState().getPrecio()) + " PC " + GOLD + " comandos: " + AQUA +
                "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadToComprar + " cantidad de la empresa del server: "
                + getState().getEmpresa() + " a " + GREEN + FORMATEA.format(getState().getPrecio()) + "PC");
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " cantidad de " + this.getState().getEmpresa(),
                GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(getState().getPrecio()) + " PC",
                GOLD + "Total: " + GREEN + FORMATEA.format(getState().getPrecio() * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + FORMATEA.format(this.compradorJugador.getPixelcoins()) + " PC"
        );
    }

    @Override
    public double maxValue() {
        return this.compradorJugador.getPixelcoins() >= (getState().getCantidad() * getState().getPrecio()) ?
                getState().getCantidad() :
                compradorJugador.getPixelcoins() / getState().getPrecio();
    }

    @Override
    public double initialValue() {
        return 1;
    }
}
