package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Transacciones;
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

        ItemStack info = buildItemInfo();
        ItemStack lapis = buildItemLapis(dineroJugador);
        ItemStack cuarzo = buildItemCuarzo(dineroJugador);
        ItemStack diamantes = buildItemDiamante(dineroJugador);

        inventory.setItem(4, info);
        inventory.setItem(10, diamantes);
        inventory.setItem(13, lapis);
        inventory.setItem(16, cuarzo);

        return inventory;
    }

    private ItemStack buildItemInfo () {
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Puedes convertir todas tus pixelcoins");
        lore.add("en el mayor numero posible de diamantes");
        lore.add("cuerzo o lapislazuli");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private ItemStack buildItemLapis (double dineroJugador) {
        ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta lapisMeta = lapis.getItemMeta();

        lapisMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE LAPISLAZULI");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "1 DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI + " PC");
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        lapisMeta.setLore(lore);
        lapis.setItemMeta(lapisMeta);

        return lapis;
    }

    private ItemStack buildItemCuarzo (double dineroJugador) {
        ItemStack cuarzo = new ItemStack(Material.QUARTZ_BLOCK);
        ItemMeta cuarzoMeta = cuarzo.getItemMeta();

        cuarzoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE CUARZO");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "1 BLOQUE DE CUARZO -> " + ChatColor.GREEN + Transacciones.CUARZO + " PC");
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        cuarzoMeta.setLore(lore);
        cuarzo.setItemMeta(cuarzoMeta);

        return cuarzo;
    }

    private ItemStack buildItemDiamante (double dineroJugador) {
        ItemStack diamante = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta diamanteMeta = diamante.getItemMeta();

        diamanteMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE DIAMANTES");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE);
        lore.add("    ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        diamanteMeta.setLore(lore);
        diamante.setItemMeta(diamanteMeta);

        return diamante;
    }
}
