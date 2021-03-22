package es.serversurvival.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import sun.plugin.com.Utils;

import java.util.List;

public final class MinecraftUtils {
    private MinecraftUtils() {}

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

    public static Scoreboard createScoreboard (String name, String displayname) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(displayname);

        return scoreboard;
    }

    public static void addLineToScoreboard (Objective objective, String message, int fila) {
        Score score = objective.getScore(message);
        score.setScore(fila);
    }
}
