package es.serversurvival.jugadores.withers.sacarItem;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SacarItemInventoryFactory extends InventoryFactory {
    private final double pixelcoinsJugador;

    public SacarItemInventoryFactory(double pixelcoinsJugador) {
        this.pixelcoinsJugador = pixelcoinsJugador;
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "   ELIGE ITEM PARA SACAR");

        inventory.setItem(4, buildItemInfo());

        inventory.setItem(9, buildItem("DIAMANTE", CambioPixelcoins.DIAMANTE, Material.DIAMOND));
        inventory.setItem(11, buildItem("BLOQUE DE DIAMANTE", CambioPixelcoins.DIAMANTE * 9, Material.DIAMOND_BLOCK));
        inventory.setItem(13, buildItem("LAPISLAZULI", CambioPixelcoins.LAPISLAZULI, Material.LAPIS_LAZULI));
        inventory.setItem(15, buildItem("BLOQUE DE LAPISLAZULI", CambioPixelcoins.LAPISLAZULI * 9, Material.LAPIS_BLOCK));
        inventory.setItem(17, buildItem("CUARZO", CambioPixelcoins.CUARZO, Material.QUARTZ_BLOCK));

        return inventory;
    }

    private ItemStack buildItemInfo() {
        String displayName = ChatColor.GOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("Una vez que tengas pixelcoins");
        lore.add("puedes intercambiarlas por estos");
        lore.add("bloques y viceversa");

        return ItemBuilder.of(Material.PAPER).title(displayName).lore(lore).build();
    }

    public ItemStack buildItem (String itemACambiar, double cambio, Material itemMaterial) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN " + itemACambiar;
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 "+itemACambiar+" -> " + ChatColor.GREEN + cambio);
        lore.add("    ");
        lore.add(ChatColor.GOLD +"Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(pixelcoinsJugador));

        return ItemBuilder.of(itemMaterial).title(displayName).lore(lore).build();
    }
}
