package es.serversurvival.eventos;

import es.serversurvival.objetos.mySQL.Mensajes;
import es.serversurvival.objetos.mySQL.Cuentas;
import es.serversurvival.objetos.mySQL.NumeroCuentas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.objetos.mySQL.tablasObjetos.NumeroCuenta;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private Mensajes m = new Mensajes();
    private ScoreboardTaskManager sp;
    private NumeroCuentas numeroCuentas = new NumeroCuentas();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player p = evento.getPlayer();
        
        m.conectar();
        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + m.getNMensajes(p.getName()) + " pendientes " + ChatColor.AQUA + "  /mensajes");

        NumeroCuenta numeroCuenta = numeroCuentas.getNumeroCuenta(p.getName());
        if(numeroCuenta == null){
            numeroCuentas.nuevoNumeroCuenta(p.getName());
        }
        m.desconectar();

        sp = new ScoreboardTaskManager();
        sp.updateScoreboard(p);
    }
}
