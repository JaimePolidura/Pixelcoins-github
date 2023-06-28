package es.serversurvival.minecraftserver._shared.menus;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public final class MenuItems {
    public static final ItemStack GO_BACK = ItemBuilder.of(Material.RED_BANNER)
            .title(RED + "<-")
            .build();

    public static final String CLICKEABLE = GOLD + "" + BOLD + UNDERLINE;
    public static final String CARGANDO = RED + "Cargando...";
}
