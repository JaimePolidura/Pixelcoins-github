package es.serversurvival.menus.menus;

import es.serversurvival.menus.menus.confirmaciones.BorrrarEmpresaConfirmacion;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.VerEmpresaInventoryFactory;
import es.serversurvival.mySQL.Empleados;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;

public class EmpresasVerMenu extends Menu implements Clickable, Refreshcable, PostLoading, CanGoBack {
    private Inventory inventory;
    private String empresa;
    private Player player;

    public EmpresasVerMenu (Player player, String empresa) {
        this.empresa = empresa;
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new VerEmpresaInventoryFactory(empresa), player.getName());

        postLoad();
    }

    public String getEmpresa (){
        return empresa;
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
        ItemStack itemClickedao = event.getCurrentItem();

        if (itemClickedao == null || !Funciones.cuincideNombre(itemClickedao.getType().toString(), "PLAYER_HEAD", "RED_WOOL", "BARRIER")) {
            return;
        }

        String nombreItem = itemClickedao.getType().toString();

        if (nombreItem.equalsIgnoreCase("RED_WOOL")) {
            EmpresasOwnerMenu menu = new EmpresasOwnerMenu((Player) event.getWhoClicked());
            menu.openMenu();
        }else if(nombreItem.equalsIgnoreCase("BARRIER")){
            BorrrarEmpresaConfirmacion confirmacion = new BorrrarEmpresaConfirmacion(player ,empresa);
            confirmacion.openMenu();
        }else{
            String nombreEmpleadoADespedir;
            if(itemClickedao.getItemMeta().getLore().get(1).split(" ")[1] == null){
                return;
            }
            nombreEmpleadoADespedir = itemClickedao.getItemMeta().getLore().get(1).split(" ")[1];

            empleadosMySQL.conectar();

            empleadosMySQL.despedir(empresa, nombreEmpleadoADespedir, "Despedido desde el menu", (Player) event.getWhoClicked());
            refresh();

            empleadosMySQL.desconectar();
        }
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void refresh() {
        Map<String, Menu> copyOfMenus = MenuManager.getCopyOfAllMenus();

        copyOfMenus.forEach((jugador, menu) ->{
            if(menu.getPlayer().getName().equalsIgnoreCase(getPlayer().getName())){
                EmpresasVerMenu menuToReopen = new EmpresasVerMenu(player, empresa);
                menuToReopen.openMenu();
            }else if(menu instanceof EmpleosMenu) {
                EmpleosMenu menuToReopen = new EmpleosMenu(player);
                menuToReopen.openMenu();
            }
        });
    }

    @Override
    public void postLoad() {
        for(int i = 1; i < 53; i++){
            ItemStack currentItem = inventory.getItem(i);

            if(currentItem == null || !currentItem.getType().toString().equalsIgnoreCase("PLAYER_HEAD")){
                break;
            }
            SkullMeta currentItemMeta = (SkullMeta) currentItem.getItemMeta();
            String empleado = currentItemMeta.getLore().get(1).split(" ")[1];

            currentItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(empleado));
            currentItem.setItemMeta(currentItemMeta);
        }
    }

    @Override
    public String getNameItemGoBack() {
        return Material.RED_WOOL.toString();
    }

    @Override
    public void goBack() {
        EmpresasOwnerMenu menu = new EmpresasOwnerMenu(player);
        menu.openMenu();
    }
}