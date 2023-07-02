package es.serversurvival.minecraftserver.bolsa.verordenespremarket;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.bolsa.cancelarordenpremarket.OrdenPremarketCancelada;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnOrdenPremarketCancelada {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void on(OrdenPremarketCancelada e) {
        enviadorMensajes.enviarMensaje(e.getJugadorId(), GOLD + "Has cancelado la orden");
    }
}
