package es.serversurvival.menus.menus;

import es.serversurvival.menus.menus.CanGoBack;
import es.serversurvival.menus.menus.CanGoFordward;
import es.serversurvival.menus.menus.Clickable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import java.util.*;

public interface Paginated extends CanGoBack, CanGoFordward {
    String ITEM_NAME_GOFORDWARD = ChatColor.GREEN + "" + ChatColor.BOLD + "-->";
    String ITEM_NAME_GOBACK = ChatColor.RED + "" + ChatColor.BOLD + "<--";

    int getCurrentIndex();
    List<Page> getPages();

    default void performClick (String nombreItem) {
        if(nombreItem == null) return;

        if(nombreItem.equalsIgnoreCase(getNameItemGoBack())){
            goBack();
        }else if(nombreItem.equalsIgnoreCase(getNameItemGoFordward())){
            goFordward();
        }
    }

    class Page {
        public int index;
        public Inventory inventory;

        public Page (int index, Inventory inventory) {
            this.index = index;
            this.inventory = inventory;
        }
    }
}
