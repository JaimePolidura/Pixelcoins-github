package es.serversurvival._shared.eventosminecraft;

import es.serversurvival.mensajes._shared.mysql.Mensajes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    private final Mensajes mensajesMySQL = Mensajes.INSTANCE;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + mensajesMySQL.getMensajesJugador(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");
    }
}
