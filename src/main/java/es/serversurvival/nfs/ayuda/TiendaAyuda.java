package es.serversurvival.nfs.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.mySQL.Ofertas;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command("ayuda tienda")
public class TiendaAyuda implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.GOLD + "En la tienda puedes comprar y vender objetos de manera segura y rapida sin requerir de que el vendedor este online. Se accede con /tienda o /warp spawn y clickeando al NPC llamando tienda.");
        sender.sendMessage("          ");
        sender.sendMessage("/vender <precio de pixelcoins>" + ChatColor.GOLD + "Con este podras lanzar a la venta el objeto que tengas en la mano a un determinado precio en la tienda, solo podras tener " + Ofertas.MAX_ESPACIOS + " como maximo en la tienda a le vez.");
        sender.sendMessage("          ");
        sender.sendMessage("/tienda " + ChatColor.GOLD + "Con este comando podras acceder a la tienda");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.GOLD + " Para comprar objetos tendras que darle click izquierdo y automaticamente se te comprar y se añadara al inventario.");
        sender.sendMessage("          ");
        sender.sendMessage(ChatColor.GOLD + "Para quitarlos click izquierdo en tus objetos que esten en la venta.");
        sender.sendMessage("          ");
    }
}
