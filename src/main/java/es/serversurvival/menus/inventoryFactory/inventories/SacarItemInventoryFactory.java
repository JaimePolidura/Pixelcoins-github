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

        ItemStack info = buildItemInfo();
        ItemStack diamante = buildItemDiamante(dineroJugador);
        ItemStack bloqueDiamante = buildItemDiamanteBloque(dineroJugador);
        ItemStack cuarzo = buildItemCuarzo(dineroJugador);
        ItemStack lapis = buildItemLapis(dineroJugador);
        ItemStack lapisBloque = buildItemLapisBloque(dineroJugador);

        inventory.setItem(4, info);
        inventory.setItem(9, diamante);
        inventory.setItem(11, bloqueDiamante);
        inventory.setItem(13, lapis);
        inventory.setItem(15, lapisBloque);
        inventory.setItem(17, cuarzo);

        return inventory;
    }

    private ItemStack buildItemInfo() {
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "INFO");
        List<String> lore = new ArrayList<>();
        lore.add("Una vez que tengas pixelcoins");
        lore.add("puedes intercambiarlas por estos");
        lore.add("bloques y viceversa");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private ItemStack buildItemDiamante (double dineroJugador) {
        ItemStack diamante = new ItemStack(Material.DIAMOND);
        ItemMeta diamanteMeta = diamante.getItemMeta();

        diamanteMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN DIAMANTE");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE);
        lore.add("    ");
        lore.add(ChatColor.GOLD +"Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        diamanteMeta.setLore(lore);
        diamante.setItemMeta(diamanteMeta);

        return diamante;
    }

    private ItemStack buildItemDiamanteBloque (double dineroJugador) {
        ItemStack bloqueDiamante = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta bloqueDiamanteMeta = bloqueDiamante.getItemMeta();

        bloqueDiamanteMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE DIAMANTE");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 BLOQUE DE DIAMANTE -> " + ChatColor.GREEN + Transacciones.DIAMANTE * 9 + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        bloqueDiamanteMeta.setLore(lore);
        bloqueDiamante.setItemMeta(bloqueDiamanteMeta);

        return bloqueDiamante;
    }

    private ItemStack buildItemCuarzo (double dineroJugador) {
        ItemStack cuarzo = new ItemStack(Material.QUARTZ_BLOCK);
        ItemMeta cuarzoMeta = cuarzo.getItemMeta();

        cuarzoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE CUARZO");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "1 BLOQUE DE CUARZO -> " + ChatColor.GREEN + Transacciones.CUARZO + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        cuarzoMeta.setLore(lore);
        cuarzo.setItemMeta(cuarzoMeta);

        return cuarzo;
    }

    private ItemStack buildItemLapis (double dineroJugador) {
        ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
        ItemMeta lapisMeta = lapis.getItemMeta();

        lapisMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR LAPISLAZULI");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_BLUE + "1 DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        lapisMeta.setLore(lore);
        lapis.setItemMeta(lapisMeta);

        return lapis;
    }

    private ItemStack buildItemLapisBloque (double dineroJugador) {
        ItemStack lapisBloque = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta lapisBloqueMeta = lapisBloque.getItemMeta();

        lapisBloqueMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN BLOQUE DE LAPISLAZULI");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "1 BLOQUE DE LAPISLAZULI -> " + ChatColor.GREEN + Transacciones.LAPISLAZULI * 9 + " PC");
        lore.add("    ");
        lore.add(ChatColor.GOLD + "Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(dineroJugador));

        lapisBloqueMeta.setLore(lore);
        lapisBloque.setItemMeta(lapisBloqueMeta);

        return lapisBloque;
    }
}
