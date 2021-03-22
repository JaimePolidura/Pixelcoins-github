package es.serversurvival.menus.menus;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.PerfilInventoryFactory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PerfilMenu extends Menu implements Clickable{
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "           TU PERFIL";
    private Inventory inventory;
    private Player player;

    public PerfilMenu (Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(new PerfilInventoryFactory(), player.getName());
        this.player = player;
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
    public void onClick(InventoryClickEvent event) {
        ItemStack itemClicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if(itemClicked == null) return;

        switch (itemClicked.getType().toString()){
            case "DIAMOND_SWORD":
                DeudasMenu menu = new DeudasMenu(player);
                menu.openMenu();
                break;

            case "BOOK":
                BolsaCarteraMenu menuPosAbiertas = new BolsaCarteraMenu(player);
                menuPosAbiertas.openMenu();
                break;

            case "GOLD_BLOCK":
                EmpresasOwnerMenu menuEmpresas = new EmpresasOwnerMenu(player);
                menuEmpresas.openMenu();
                break;

            case "GOLDEN_APPLE":
                EmpleosMenu menuEmpleos = new EmpleosMenu(player);
                menuEmpleos.openMenu();
                break;

            case "PLAYER_HEAD":
                TopMenu menuTop = new TopMenu(player);
                menuTop.openMenu();
                break;

            case "CHEST":
                OfertasMenu menuOfertas = new OfertasMenu(player);
                menuOfertas.openMenu();
                break;
        }
    }
}