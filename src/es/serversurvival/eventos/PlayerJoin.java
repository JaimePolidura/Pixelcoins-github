package es.serversurvival.eventos;

import es.serversurvival.objetos.Mensajes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private Mensajes m = new Mensajes();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player p = evento.getPlayer();
        this.mostrarNoticias(p);

        try {
            m.conectar("root", "", "pixelcoins");
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + m.getNMensajes(p.getName()) + " pendientes " + ChatColor.AQUA + "  /mensajes");
            m.desconectar();
        } catch (Exception e) {

        }
    }

    //Noticias
    private void mostrarNoticias(Player p) {
        p.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "                    ACTUALIZACIONES:   (12/01/2020)");
        p.sendMessage(ChatColor.AQUA + "- " + "Se ha a�adido la posibilidad de endeudarte y de prestar pixelcoins y de ganar dinero con ello. Mas info en: /ayuda deudas");
        p.sendMessage(ChatColor.AQUA + "- " + "Ahora ya se puede vender libros encantados en la tienda");
        p.sendMessage(ChatColor.AQUA + "- " + "Se ha a�adido un nuevo espacio en la tienda! ahora puedes tener hasta cuatro");
    }
}
