package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Ofertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VenderComando extends Comando {
    private final String CNombre = "vender";
    private final String sintaxis = "/vender <precio>";
    private final String ayuda = "vender objetos en la tienda a un precio, para retirarlos en /tienda";

    public String getCNombre() {
        return CNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        Ofertas o = new Ofertas();
        //comprobamos: args=1, que no sea pociones,baners o libro encantdo, que sea texto el argumento, y que el precio sea positivo
        if (args.length == 1) {
            String sprecio = args[0];
            ItemStack i = p.getInventory().getItemInMainHand();
            String nombreItem = i.getType().toString();
            double precio;

            if (!nombreItem.equals("POTION") && !nombreItem.contentEquals("BANNER") && !nombreItem.equals("AIR") && !nombreItem.equals("SPLASH_POTION") && !nombreItem.contentEquals("LINGERING_POTION")) {
                try {
                    precio = Double.parseDouble(sprecio);
                    if (precio > 0) {
                        o.conectar();
                        o.crearOferta(i, p, precio);
                        o.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "A ser posible que los precios no sean negativos ");
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /vender <precio a vender/item>");
                }
            } else {
                p.sendMessage(ChatColor.DARK_RED + "No se puede vender ese tipo de objeto");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /vender <precio a vender en la tienda>");
        }
    }
}