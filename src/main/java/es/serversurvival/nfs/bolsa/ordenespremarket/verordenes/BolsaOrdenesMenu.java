package es.serversurvival.nfs.bolsa.ordenespremarket.verordenes;

import es.serversurvival.nfs.shared.menus.Menu;
import es.serversurvival.nfs.shared.menus.inventory.InventoryCreator;
import es.serversurvival.nfs.shared.menus.Clickable;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BolsaOrdenesMenu extends Menu implements Clickable {
    private final Inventory inventory;
    private final Player player;

    public BolsaOrdenesMenu(Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new BolsaOrdenesInventoryFactory(), player.getName());

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

    @Override
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClicked = event.getCurrentItem();

        if(Funciones.noEsDeTipoItem(itemClicked, "NAME_TAG", "REDSTONE_TORCH")){
            return;
        }

        int id = getIdOrndeFromItem(itemClicked);
        if(id != -1){

            AllMySQLTablesInstances.ordenesMySQL.cancelarOrden(id, (Player) event.getWhoClicked());
            closeMenu();

        }
    }

    private int getIdOrndeFromItem (ItemStack item) {
        try {
            return Integer.parseInt(item.getItemMeta().getLore().get(5));
        } catch (Exception e){
            return -1;
        }
    }
}
