package es.serversurvival.menus.menus;

import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.ElegirInversionInventoryFactory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ElegirInversionMenu extends Menu implements Clickable {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + " ¿En que quieres invertir?";
    private Inventory inventory;
    private Player player;

    public ElegirInversionMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new ElegirInversionInventoryFactory(), player.getName());
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
    public void onClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !Funciones.cuincideNombre(itemStack.getType().toString(), "GOLD_INGOT", "PAPER", "CHARCOAL")){
            return;
        }

        if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase( ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones" )){
            AccionesMenu menu = new AccionesMenu(player);
        }else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase( ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas" )){
            CriptomonedasMenu menu = new CriptomonedasMenu(player);
        }else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase( ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas" )) {
            MateriasPrimasMenu menu = new MateriasPrimasMenu(player);
        }
    }
}
