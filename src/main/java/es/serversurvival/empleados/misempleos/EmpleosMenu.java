package es.serversurvival.empleados.misempleos;

import es.serversurvival.empleados.irse.IrseEmpresaUseCase;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.CanGoBack;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Refreshcable;
import es.serversurvival.empresas.miempresa.EmpresasVerMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EmpleosMenu extends Menu implements Clickable, Refreshcable, CanGoBack {
    private final IrseEmpresaUseCase irseEmpresaUseCase = IrseEmpresaUseCase.INSTANCE;

    private Player player;
    private Inventory inventory;

    public EmpleosMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new EmpleosInventoryFactory(), player.getName());
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
        if(event.getCurrentItem() == null || event.getCurrentItem().getType().toString().equalsIgnoreCase("AIR")){
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if(currentItem.getItemMeta().getLore().get(1) == null){
            return;
        }

        String nombreEmpresa = currentItem.getItemMeta().getLore().get(1).split(" ")[1];

        irseEmpresaUseCase.irse(player.getName(), nombreEmpresa);
        refresh();
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void refresh() {
        Map<String, Menu> copiaMenus = MenuManager.getCopyOfAllMenus();
        copiaMenus.forEach( (jugador, menu) -> {
            if(menu instanceof EmpresasVerMenu){
                EmpresasVerMenu menuToReopen = new EmpresasVerMenu(Bukkit.getPlayer(jugador),  ((EmpresasVerMenu) menu).getEmpresa() );
                menuToReopen.openMenu();
            }else if (menu instanceof EmpleosMenu){
                EmpleosMenu menuToReopen = new EmpleosMenu(Bukkit.getPlayer(jugador));
                menuToReopen.openMenu();
            }
        });
    }

    @Override
    public String getNameItemGoBack() {
        return Material.RED_WOOL.toString();
    }

    @Override
    public void goBack() {
        PerfilMenu menu = new PerfilMenu(player);
    }
}
