package es.serversurvival.minecraftserver._shared.menus;

import es.bukkitbettermenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public final class MenuItems {
    public static final ItemStack GO_FORWARD_PAGE = ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "->").build();
    public static final ItemStack GO_BACKWARD_PAGE = ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "<-").build();

    public static final String GO_BACK_TITLE = RED + "<-";
    public static final String INFO = GOLD + "" + BOLD + "INFO";
    public static final String CLICKEABLE = GOLD + "" + BOLD + UNDERLINE;
    public static final String CARGANDO = RED + "Cargando...";
    public static final String TITULO_MENU = DARK_RED + "" + BOLD;
    public static final String TITULO_ITEM = GOLD + "" + BOLD;

    public static final ItemStack GO_MENU_BACK = ItemBuilder.of(Material.RED_BANNER)
            .title(GO_BACK_TITLE)
            .build();
}
