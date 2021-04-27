package es.serversurvival.nfs.jugadores.top;

import es.serversurvival.nfs.shared.menus.Menu;
import es.serversurvival.nfs.shared.menus.inventory.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TopMenu extends Menu {
    private Player player;
    private Inventory inventory;

    public TopMenu(Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(new TopInventoryFactory(), player.getName());
        this.player = player;
        openMenu();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
