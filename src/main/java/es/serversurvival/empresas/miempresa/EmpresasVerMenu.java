package es.serversurvival.empresas.miempresa;

import es.serversurvival.empleados.misempleos.EmpleosMenu;
import es.serversurvival.empresas.borrar.BorrrarEmpresaConfirmacion;
import es.serversurvival.empresas.pagardividendos.PagarDividendoConfirmacion;
import es.serversurvival.empresas.vertodas.EmpresasVerTodasMenu;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival.shared.menus.*;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
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
        openMenu();
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
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClickedao = event.getCurrentItem();

        if (itemClickedao == null || !Funciones.cuincideNombre(itemClickedao.getType().toString(), "PLAYER_HEAD", "RED_WOOL", "BARRIER", "GOLD_INGOT")) {
            return;
        }

        String nombreItem = itemClickedao.getType().toString();

        if (nombreItem.equalsIgnoreCase("RED_WOOL")) {
            PerfilMenu perfilMenu = new PerfilMenu((Player) event.getWhoClicked());
        }else if(nombreItem.equalsIgnoreCase("BARRIER")){
            BorrrarEmpresaConfirmacion confirmacion = new BorrrarEmpresaConfirmacion(player ,empresa);
        }else if(nombreItem.equalsIgnoreCase("GOLD_INGOT")) {
            PagarDividendoConfirmacion pagarDividendoConfirmacion = new PagarDividendoConfirmacion(player, empresa);

        }else{
            String nombreEmpleadoADespedir;
            if(itemClickedao.getItemMeta().getLore().get(1).split(" ")[1] == null){
                return;
            }
            nombreEmpleadoADespedir = itemClickedao.getItemMeta().getLore().get(1).split(" ")[1];

            AllMySQLTablesInstances.empleadosMySQL.despedir(empresa, nombreEmpleadoADespedir, "Despedido desde el menu", (Player) event.getWhoClicked());
            refresh();

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
        EmpresasVerTodasMenu empresasVerTodasMenu = new EmpresasVerTodasMenu(player);
    }
}
