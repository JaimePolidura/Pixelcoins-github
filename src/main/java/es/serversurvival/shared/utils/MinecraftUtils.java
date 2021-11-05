package es.serversurvival.shared.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public final class MinecraftUtils {
    private MinecraftUtils() {}

    public static void setLore (ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setLoreAndDisplayName (ItemStack itemStack, List<String> lore, String displayname) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);
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
