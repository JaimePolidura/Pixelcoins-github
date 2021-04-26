package es.serversurvival.nfs.deudas.ver;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.MenuManager;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.legacy.menus.inventoryFactory.inventories.DeudasInventoryFactory;
import es.serversurvival.legacy.menus.menus.CanGoBack;
import es.serversurvival.legacy.menus.menus.Clickable;
import es.serversurvival.legacy.menus.menus.Refreshcable;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.deudas.mysql.Deuda;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.jugadores.perfil.PerfilMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DeudasMenu extends Menu implements Clickable, Refreshcable, CanGoBack {
    public final static String tiulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "          TUS DEUDAS";
    private Player player;
    private Inventory inventory;

    public DeudasMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new DeudasInventoryFactory(), player.getName());
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
        ItemStack itemCliqueado = event.getCurrentItem();

        boolean notValidClick = itemCliqueado == null || !Funciones.cuincideNombre(itemCliqueado.getType().toString(), "RED_BANNER", "GREEN_BANNER");
        if(notValidClick){
            return;
        }

        performClick(itemCliqueado);
    }

    private void performClick (ItemStack itemCliqueado) {
        int idAPagar = Integer.parseInt(itemCliqueado.getItemMeta().getLore().get(4).split(" ")[1]);
        String tipoItem = itemCliqueado.getType().toString();

        if(tipoItem.equalsIgnoreCase("RED_BANNER")){
            pagarDeuda(idAPagar);
        }else{
            cancelarDeuda(idAPagar);
        }

    }

    private void pagarDeuda (int id) {
        Deuda deudaAPagar = AllMySQLTablesInstances.deudasMySQL.getDeuda(id);

        Player jugadorQueVaAPagar = Bukkit.getPlayer(deudaAPagar.getDeudor());
        double pixelcoinsAPagar = deudaAPagar.getPixelcoins_restantes();

        if(AllMySQLTablesInstances.jugadoresMySQL.getJugador(jugadorQueVaAPagar.getName()).getPixelcoins() < pixelcoinsAPagar){
            jugadorQueVaAPagar.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
            jugadorQueVaAPagar.playSound(jugadorQueVaAPagar.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        AllMySQLTablesInstances.deudasMySQL.pagarDeuda(jugadorQueVaAPagar, id);
        refresh();
    }

    private void cancelarDeuda (int id) {
        Deuda deudaACancelar = AllMySQLTablesInstances.deudasMySQL.getDeuda(id);
        Player jugadorQueVaACancelar = Bukkit.getPlayer(deudaACancelar.getAcredor());

        AllMySQLTablesInstances.deudasMySQL.cancelarDeuda(jugadorQueVaACancelar, id);
        refresh();
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void refresh() {
        DeudasInventoryFactory factory = new DeudasInventoryFactory();
        Map<String, Menu> copyOfMenus = MenuManager.getCopyOfAllMenus();

        copyOfMenus.forEach( (jugador, menu) -> {
            if(menu instanceof DeudasMenu){
                Inventory inventory = InventoryCreator.createInventoryMenu(factory, jugador);

                ((Refreshcable) menu).setInventory(inventory);
                MenuManager.nuevoMenu(jugador, menu);
                MenuManager.getByPlayer(jugador).openMenu();
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
