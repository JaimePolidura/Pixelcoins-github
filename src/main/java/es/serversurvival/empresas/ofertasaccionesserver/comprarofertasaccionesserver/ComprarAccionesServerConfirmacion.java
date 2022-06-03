package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class ComprarAccionesServerConfirmacion extends NumberSelectorMenu {
    private final OfertaAccionServer oferta;
    private final Jugador compradorJugador;
    private final JugadoresService jugadoresService;

    public ComprarAccionesServerConfirmacion(OfertaAccionServer oferta, Player player) {
        this.oferta = oferta;
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.compradorJugador = this.jugadoresService.getByNombre(player.getName());
    }

    @Override
    public void onAccept(Player player, InventoryClickEvent event) {
        int cantidadToComprar = (int) super.getPropertyDouble("cantidad");

        new ComprarOfertaMercadoUseCase().comprarOfertaMercadoAccionServer(player.getName(),
                oferta.getOfertaAccionServerId(), cantidadToComprar);

        enviarMensajeYSonido(player, GOLD + "Has comprado " + FORMATEA.format(cantidadToComprar) + " acciones a " + GREEN +
                FORMATEA.format(oferta.getPrecio()) + " PC" + GOLD + " que es un total de " + GREEN + FORMATEA.format(
                cantidadToComprar * oferta.getPrecio()) + " PC " + GOLD + " comandos: " + AQUA +
                "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadToComprar + " acciones de la empresa del server: "
                + oferta.getEmpresa() + " a " + GREEN + FORMATEA.format(oferta.getPrecio()) + "PC");
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " acciones de " + this.oferta.getEmpresa(),
                GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC",
                GOLD + "Total: " + GREEN + FORMATEA.format(oferta.getPrecio() * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + FORMATEA.format(this.compradorJugador.getPixelcoins()) + " PC"
        );
    }

    @Override
    public double maxValue() {
        return this.compradorJugador.getPixelcoins() >= (oferta.getCantidad() * oferta.getPrecio()) ?
                oferta.getCantidad() :
                compradorJugador.getPixelcoins() / oferta.getPrecio();
    }

    @Override
    public double initialValue() {
        return 1;
    }
}
