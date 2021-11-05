package es.serversurvival.jugadores.top;

import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TopMenu extends Menu {
    private final Player player;
    private final Inventory inventory;

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
