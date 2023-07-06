package es.serversurvival.minecraftserver._shared;

import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class MinecraftUtils {
    private MinecraftUtils() {}

    public static void enviarUrl(Player player, String url, String mensaje) {
        TextComponent message = new TextComponent(MenuItems.CLICKEABLE + mensaje);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        player.spigot().sendMessage(message);
    }

    public static void broadcastExcept(UUID playerId, String mensaje) {
        Player player = Bukkit.getPlayer(playerId);

        if(player == null){
            Bukkit.broadcastMessage(mensaje);
        }

        broadcastExcept(player, mensaje);
    }

    public static void broadcastExcept(Player playerException, String mensaje) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.equals(playerException))
                .forEach(player -> player.sendMessage(mensaje));
    }

    public static void enviarMensajeYSonido (Player player, String mensaje, Sound sound) {
        player.sendMessage(mensaje);
        player.playSound(player.getLocation(), sound, 10, 1);
    }

    public static ItemStack setLore (ItemStack itemStack, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static UUID getLastLineOfLore(ItemStack itemStack, int indexStaringFromLast) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        return UUID.fromString(lore.get(lore.size() - indexStaringFromLast - 1));
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

    public static void setLineToScoreboard(Objective objective, String message, int fila) {
        Score score = objective.getScore(message);
        score.setScore(fila);
    }

    public static int getEspaciosOcupados(Inventory inventory) {
        int espaciosLibres = 36;
        ItemStack[] items = inventory.getContents();

        for(int i = 0; i < 36; i++){
            if(items[i] == null || esDeTipoItem(items[i], "AIR")) {
                espaciosLibres--;
            }
        }

        return espaciosLibres;
    }

    public static boolean esDeTipoItem(ItemStack item, String...tipos) {
        return cuincideNombre(item.getType().toString(), tipos);
    }

    private static boolean cuincideNombre (String nombre, String... items){
        List<String> bannedNamesList = Arrays.asList(items);

        return bannedNamesList.stream()
                .anyMatch( (name) -> name.equalsIgnoreCase(nombre));
    }
}
