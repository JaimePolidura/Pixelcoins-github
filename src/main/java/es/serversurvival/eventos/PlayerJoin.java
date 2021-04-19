package es.serversurvival.eventos;

import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.Mensajes;
import es.serversurvival.npc.NPCManager;
import es.serversurvival.task.ScoreBoardManager;
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
