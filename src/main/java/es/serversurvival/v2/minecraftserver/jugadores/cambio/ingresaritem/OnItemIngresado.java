package es.serversurvival.v2.minecraftserver.jugadores.cambio.ingresaritem;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.jugadores.cambio.ingresarItem.ItemIngresadoEvento;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@EventHandler
@AllArgsConstructor
public final class OnItemIngresado {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void onItemIngresado (ItemIngresadoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());

        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Se ha a?adido: " + GREEN + FORMATEA.format(evento.getPixelcoins())
                + GOLD + " Tienes " + FORMATEA.format(evento.getPixelcoins() + evento.getJugador().getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
