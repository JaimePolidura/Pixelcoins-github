package es.serversurvival.menus.menus;

import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.EmpresasOwnerInventoryFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EmpresasOwnerMenu extends Menu implements Clickable, CanGoBack{
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "       TUS EMPRESAS";
    private Inventory inventory;
    private Player player;

    public EmpresasOwnerMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new EmpresasOwnerInventoryFactory(), player.getName());
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
        ItemStack itemClickeado = event.getCurrentItem();

        if(itemClickeado == null || Funciones.cuincideNombre(itemClickeado.getType().toString(), "AIR")){
            return;
        }
        
        if(itemClickeado.getType().toString().equalsIgnoreCase("BOOK")){
            EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu((Player) event.getWhoClicked());
            menu.openMenu();
        }
        String nombreEmpresaAVer;
        try{
            nombreEmpresaAVer = itemClickeado.getItemMeta().getDisplayName().split(" ")[3];
        }catch (NullPointerException | IndexOutOfBoundsException e){
            return;
        }

        EmpresasVerMenu menu = new EmpresasVerMenu((Player) event.getWhoClicked(), nombreEmpresaAVer);
        menu.openMenu();
    }

    @Override
    public String getNameItemGoBack() {
        return Material.RED_WOOL.toString();
    }

    @Override
    public void goBack() {
        PerfilMenu menu = new PerfilMenu(player);
        menu.openMenu();
    }
}