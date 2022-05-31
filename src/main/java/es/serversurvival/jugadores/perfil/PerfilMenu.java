package es.serversurvival.jugadores.perfil;

import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival.jugadores.top.TopMenu;
import es.serversurvival.tienda.vertienda.OfertasMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PerfilMenu extends Menu implements Clickable {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "           TU PERFIL";
    private Inventory inventory;
    private Player player;

    public PerfilMenu(Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(new PerfilInventoryFactory(), player.getName());
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

    @Override
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if(itemClicked == null) return;

        switch (itemClicked.getType().toString()){
            case "DIAMOND_SWORD":
                break;

            case "BOOK":
                break;

            case "GOLD_BLOCK":
                break;

            case "GOLDEN_APPLE":
                break;

            case "PLAYER_HEAD":
                TopMenu menuTop = new TopMenu(player);
                break;

            case "CHEST":
                OfertasMenu menuOfertas = new OfertasMenu(player);
                break;
        }
    }
}
