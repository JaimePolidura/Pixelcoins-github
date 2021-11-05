package es.serversurvival.bolsa.valores;

import es.serversurvival.bolsa.valores.acciones.AccionesMenu;
import es.serversurvival.bolsa.valores.materiasprimas.MateriasPrimasMenu;
import es.serversurvival.shared.menus.Menu;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import es.serversurvival.shared.menus.Clickable;
import es.serversurvival.bolsa.valores.criptomonedas.CriptomonedasMenu;
import es.serversurvival.shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !Funciones.cuincideNombre(itemStack.getType().toString(), "GOLD_INGOT", "PAPER", "CHARCOAL")){
            return;
        }

        if(itemStack.getType() == Material.PAPER){
            AccionesMenu menu = new AccionesMenu(player);
        }else if (itemStack.getType() == Material.GOLD_INGOT) {
            CriptomonedasMenu menu = new CriptomonedasMenu(player);
        }else if (itemStack.getType() == Material.CHARCOAL) {
            MateriasPrimasMenu menu = new MateriasPrimasMenu(player);
        }
    }
}
