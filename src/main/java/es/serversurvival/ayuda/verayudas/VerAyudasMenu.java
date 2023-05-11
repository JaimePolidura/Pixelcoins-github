package es.serversurvival.ayuda.verayudas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class VerAyudasMenu extends Menu {
    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 1, 0, 0, 0  },
                {2, 0, 3, 0, 4, 0, 5, 0, 6  },
                {0, 0, 0, 7, 0, 8, 0, 0, 0  }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .staticMenu()
                .title(ChatColor.DARK_RED + "" + ChatColor.BOLD + "CLICK para ver las ayudas")
                .item(1, buildItemAyuda("BOLSA"), (player, e) -> this.onItemAyudaClicked(player, "bolsa help"))
                .item(2, buildItemAyuda("JUGAR"), (player, e) -> this.onItemAyudaClicked(player, "jugar"))
                .item(3, buildItemAyuda("PIXELCOINS"), (player, e) -> this.onItemAyudaClicked(player, "ayuda"))
                .item(4, buildItemAyuda("NORMAS"), (player, e) -> this.onItemAyudaClicked(player, "normas"))
                .item(5, buildItemAyuda("TIENDA"), (player, e) -> this.onItemAyudaClicked(player, "tienda help"))
                .item(6, buildItemAyuda("DEUDA"), (player, e) -> this.onItemAyudaClicked(player, "deuda help"))
                .item(7, buildItemAyuda("EMPRESARIO"), (player, e) -> this.onItemAyudaClicked(player, "bolsa help"))
                .item(8, buildItemAyuda("EMPLEO"), (player, e) -> this.onItemAyudaClicked(player, "empleos help"))
                .build();
    }

    private void onItemAyudaClicked(Player player, String command){
        player.performCommand(command);
        player.closeInventory();
    }

    private ItemStack buildItemAyuda (String tiposAyuda) {
        return ItemBuilder.of(Material.WRITABLE_BOOK).title(ChatColor.GOLD + "" + ChatColor.BOLD + tiposAyuda).build();
    }
}
