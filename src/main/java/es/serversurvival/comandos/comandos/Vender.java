package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Ofertas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Vender extends Comando {
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
        if(haSidoComprado(itemAVender)){
            p.sendMessage(ChatColor.DARK_RED + "No puedes revender objetos que compraste en la tienda");
            return;
        }

        MySQL.conectar();
        ofertasMySQL.crearOferta(itemAVender, p, precio);
        MySQL.desconectar();
    }

    private boolean haSidoComprado (ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();

        if(lore == null || lore.size() == 0)
            return false;

        return lore.get(0).equalsIgnoreCase("Comprado en la tienda");
    }
}