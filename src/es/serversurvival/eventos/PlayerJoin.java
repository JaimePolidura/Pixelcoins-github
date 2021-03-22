package es.serversurvival.eventos;

import es.serversurvival.objetos.mySQL.Mensajes;
import es.serversurvival.objetos.task.ScoreboardPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private Mensajes m = new Mensajes();
    private ScoreboardPlayer sp;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player p = evento.getPlayer();

        m.conectar();
        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + m.getNMensajes(p.getName()) + " pendientes " + ChatColor.AQUA + "  /mensajes");
        m.desconectar();

        sp = new ScoreboardPlayer();
        sp.updateScoreboard(p);
    }
}
