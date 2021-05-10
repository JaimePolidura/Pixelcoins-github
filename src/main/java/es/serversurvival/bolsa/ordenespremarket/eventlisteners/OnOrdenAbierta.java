package es.serversurvival.bolsa.ordenespremarket.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.OrdenAbiertaEvento;
import es.serversurvival.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

public final class OnOrdenAbierta {
    @EventListener
    public void on(OrdenAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        Funciones.enviarMensajeYSonido(player, GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                AQUA + "/bolsa ordenes", ENTITY_PLAYER_LEVELUP);
    }
}
