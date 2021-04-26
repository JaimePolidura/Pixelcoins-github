package es.serversurvival.legacy.eventos;

import es.serversurvival.nfs.jugadores.mySQL.Jugadores;
import es.serversurvival.nfs.mensajes.mysql.Mensajes;
import es.serversurvival.legacy.npc.NPCManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    private final Mensajes mensajesMySQL = Mensajes.INSTANCE;
    private final Jugadores jugadoresMySQL = Jugadores.INSTANCE;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();

        jugadoresMySQL.setUpJugadorUnido(player);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + mensajesMySQL.getMensajesJugador(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");

        NPCManager.showPlayer(player);
    }
}
