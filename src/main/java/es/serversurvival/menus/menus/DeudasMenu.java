package es.serversurvival.menus.menus;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.DeudasInventoryFactory;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
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
        ItemStack currentItem = event.getCurrentItem();

        if(currentItem == null || !Funciones.cuincideNombre(currentItem.getType().toString(), "RED_BANNER", "GREEN_BANNER")){
            return;
        }

        int idAPagar = Integer.parseInt(currentItem.getItemMeta().getLore().get(4).split(" ")[1]);
        String tipoItem = currentItem.getType().toString();

        System.out.println(tipoItem);

        MySQL.conectar();
        if(tipoItem.equalsIgnoreCase("RED_BANNER")){
            pagarDeuda(idAPagar);
        }else{
            cancelarDeuda(idAPagar);
        }
        MySQL.desconectar();
    }

    private void pagarDeuda (int id) {
        Deuda deudaAPagar = deudasMySQL.getDeuda(id);

        Player jugadorQueVaAPagar = Bukkit.getPlayer(deudaAPagar.getDeudor());
        double pixelcoinsAPagar = deudaAPagar.getPixelcoins_restantes();

        if(jugadoresMySQL.getJugador(jugadorQueVaAPagar.getName()).getPixelcoins() < pixelcoinsAPagar){
            jugadorQueVaAPagar.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
            jugadorQueVaAPagar.playSound(jugadorQueVaAPagar.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        deudasMySQL.pagarDeuda(jugadorQueVaAPagar, id);
        refresh();
    }

    private void cancelarDeuda (int id) {
        Deuda deudaACancelar = deudasMySQL.getDeuda(id);
        Player jugadorQueVaACancelar = Bukkit.getPlayer(deudaACancelar.getAcredor());

        deudasMySQL.cancelarDeuda(jugadorQueVaACancelar, id);
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
        menu.openMenu();
    }
}
