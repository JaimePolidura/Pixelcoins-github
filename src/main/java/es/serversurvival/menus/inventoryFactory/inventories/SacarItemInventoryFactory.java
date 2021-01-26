package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SacarItemInventoryFactory extends InventoryFactory {
    private double pixelcoinsJugador;

    public SacarItemInventoryFactory (double pixelcoinsJugador) {
        this.pixelcoinsJugador = pixelcoinsJugador;
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "   ELIGE ITEM PARA SACAR");

        MySQL.conectar();
        double dineroJugador = jugadoresMySQL.getJugador(jugador).getPixelcoins();
        MySQL.desconectar();

        inventory.setItem(4, buildItemInfo());
        inventory.setItem(9, buildItemDiamante(dineroJugador));
        inventory.setItem(11, buildItemDiamanteBloque(dineroJugador));
        inventory.setItem(13, buildItemLapis(dineroJugador));
        inventory.setItem(15, buildItemLapisBloque(dineroJugador));
        inventory.setItem(17, buildItemCuarzo(dineroJugador));

        return inventory;
    }

    private ItemStack buildItemInfo() {
        String displayName = ChatColor.GOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("Una vez que tengas pixelcoins");
        lore.add("puedes intercambiarlas por estos");
        lore.add("bloques y viceversa");

        return MinecraftUtils.loreDisplayName(Material.PAPER, displayName, lore);
    }

    private ItemStack buildItemDiamante (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN DIAMANTE";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE);
        lore.add("    ");
        lore.add(ChatColor.GOLD +"Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return MinecraftUtils.loreDisplayName(Material.DIAMOND, displayName, lore);
    }

    private ItemStack buildItemDiamanteBloque (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE DIAMANTE";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 BLOQUE DE DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE * 9 + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return MinecraftUtils.loreDisplayName(Material.DIAMOND_BLOCK, displayName, lore);

    }

    private ItemStack buildItemCuarzo (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE CUARZO";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "1 BLOQUE DE CUARZO -> " + ChatColor.GREEN + Transacciones.CUARZO + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return MinecraftUtils.loreDisplayName(Material.QUARTZ_BLOCK, displayName, lore);
    }

    private ItemStack buildItemLapis (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR LAPISLAZULI";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_BLUE + "1 DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return MinecraftUtils.loreDisplayName(Material.LAPIS_LAZULI, displayName, lore);
    }

    private ItemStack buildItemLapisBloque (double dineroJugador) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE LAPISLAZULI";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "1 BLOQUE DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI * 9 + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        return MinecraftUtils.loreDisplayName(Material.LAPIS_BLOCK, displayName, lore);
    }
}
