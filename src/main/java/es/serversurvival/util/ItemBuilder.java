package es.serversurvival.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class ItemBuilder {
    private ItemBuilder() {}

    public static ItemStack lore(Material type, List<String> lore) {
        ItemStack itemStack = new ItemStack(type);
        setLore(itemStack, lore);

        return itemStack;
    }

    public static ItemStack displayname (Material type, String displayname) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack loreDisplayName (Material type, String displayname, List<String> lore) {
        ItemStack itemStack = new ItemStack(type);
        setLoreAndDisplayName(itemStack, lore, displayname);

        return itemStack;
    }

    public static ItemStack setLore (ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack setLoreAndDisplayName (ItemStack itemStack, List<String> lore, String displayname) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
