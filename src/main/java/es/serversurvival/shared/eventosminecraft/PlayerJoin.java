package es.serversurvival.shared.eventosminecraft;

import es.serversurvival.jugadores.mySQL.Jugadores;
import es.serversurvival.jugadores.setupjugadorunido.SetUpJugadorUseCase;
import es.serversurvival.mensajes.mysql.Mensajes;
import es.serversurvival.shared.npc.NPCManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    private final SetUpJugadorUseCase setUpJugadorUnido = SetUpJugadorUseCase.INSTANCE;
    private final Mensajes mensajesMySQL = Mensajes.INSTANCE;
    private final Jugadores jugadoresMySQL = Jugadores.INSTANCE;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();

        setUpJugadorUnido.setUpJugadorUnido(player);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + mensajesMySQL.getMensajesJugador(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");

        NPCManager.showPlayer(player);
    }
}
