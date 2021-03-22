package es.serversurvival.comandos.subComandos.ayuda;

import es.serversurvival.objetos.mySQL.Ofertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TiendaAyudaSubComando extends AyudaSubCommand {
    private final String SCNombre = "tienda";
    private final String sintaxis = "/ayuda tienda";
    private final String ayuda = "";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "En la tienda puedes comprar y vender objetos de manera segura y rapida sin requerir de que el vendedor este online. Se accede con /tienda o /warp spawn y clickeando al NPC llamando tienda.");
        p.sendMessage("          ");
        p.sendMessage("/vender <precio de pixelcoins>" + ChatColor.GOLD + "Con este podras lanzar a la venta el objeto que tengas en la mano a un determinado precio en la tienda, solo podras tener " + Ofertas.maxEspacios + " como maximo en la tienda a le vez.");
        p.sendMessage("          ");
        p.sendMessage("/tienda " + ChatColor.GOLD + "Con este comando podras acceder a la tienda");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + " Para comprar objetos tendras que darle click izquierdo y automaticamente se te comprar y se añadara al inventario.");
        p.sendMessage("          ");
        p.sendMessage(ChatColor.GOLD + "Para quitarlos click izquierdo en tus objetos que esten en la venta.");
        p.sendMessage("          ");
    }
}
