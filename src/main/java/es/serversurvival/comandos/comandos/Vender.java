package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Ofertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Vender extends Comando {
    Ofertas ofertasMySQL = new Ofertas();
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
        if (args.length != 1) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /vender <precio a vender en la tienda>");
            return;
        }
        String precioString = args[0];
        ItemStack itemAVender = p.getInventory().getItemInMainHand();
        String nombreItemVender = itemAVender.getType().toString();
        if (nombreItemVender.equalsIgnoreCase("AIR")) {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un objeto en la mano");
            return;
        }
        if (Ofertas.estaBaneado(nombreItemVender)) {
            p.sendMessage(ChatColor.DARK_RED + "No se puede vender ese tipo de objeto");
            return;
        }
        if (!Funciones.esDouble(precioString)) {
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /vender <precio a vender/item>");
            return;
        }
        double precio = Double.parseDouble(precioString);
        if (precio <= 0) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible que los precios no sean negativos o 0");
            return;
        }

        ofertasMySQL.conectar();
        ofertasMySQL.crearOferta(itemAVender, p, precio);
        ofertasMySQL.desconectar();
    }
}