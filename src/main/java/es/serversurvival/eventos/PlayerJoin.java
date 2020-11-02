package es.serversurvival.eventos;

import es.serversurvival.mySQL.Jugadores;
import es.serversurvival.mySQL.Mensajes;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.NumeroCuentas;
import es.serversurvival.mySQL.JugadoresInfo;
import es.serversurvival.mySQL.tablasObjetos.NumeroCuenta;
import es.serversurvival.npc.NPCManager;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    private JugadoresInfo jugadoresInfo = JugadoresInfo.INSTANCE;
    private Mensajes mensajesMySQL = Mensajes.INSTANCE;
    private NumeroCuentas numeroCuentas = NumeroCuentas.INSTANCE;
    private Jugadores jugadoresMySQL = Jugadores.INSTANCE;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();
        MySQL.conectar();

        jugadoresInfo.setUpJugado(player);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + mensajesMySQL.getMensajesJugador(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");

        NumeroCuenta numeroCuenta = numeroCuentas.getNumeroCuenta(player.getName());
        if(numeroCuenta == null){
            numeroCuentas.nuevoNumeroCuenta(player.getName());
        }

        MySQL.desconectar();

        ScoreBoardManager.updateScoreboard(player);
        NPCManager.showPlayer(player);
    }
}
