package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SacarMaxItemInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "ELLIGE ITEM PARA SACR MAX");

        MySQL.conectar();
        double dineroJugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();
        MySQL.desconectar();

        inventory.setItem(4, buildItemInfo());
        inventory.setItem(10, buildItemDiamante(dineroJugador));
        inventory.setItem(13, buildItemLapis(dineroJugador));
        inventory.setItem(16, buildItemCuarzo(dineroJugador));

        return inventory;
    }

    private ItemStack buildItemInfo () {
        List<String> lore = new ArrayList<>();
        lore.add("Puedes convertir todas tus pixelcoins");
        lore.add("en el mayor numero posible de diamantes");
        lore.add("cuerzo o lapislazuli");

        return ItemBuilder.lore(Material.PAPER, lore);
    }

    private ItemStack buildItemLapis (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE LAPISLAZULI";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "1 DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI + " PC");
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return ItemBuilder.loreDisplayName(Material.LAPIS_LAZULI, displayName, lore);
    }

    private ItemStack buildItemCuarzo (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE CUARZO";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "1 BLOQUE DE CUARZO -> " + ChatColor.GREEN + Transacciones.CUARZO + " PC");
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return ItemBuilder.loreDisplayName(Material.QUARTZ_BLOCK, displayName, lore);
    }

    private ItemStack buildItemDiamante (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE DIAMANTES";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE);
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return ItemBuilder.loreDisplayName(Material.DIAMOND_BLOCK, displayName, lore);
    }
}
