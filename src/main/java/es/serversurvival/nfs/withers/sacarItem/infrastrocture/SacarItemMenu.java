package es.serversurvival.nfs.withers.sacarItem.infrastrocture;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.MenuManager;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.legacy.menus.menus.Clickable;
import es.serversurvival.legacy.menus.menus.Refreshcable;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.enums.CambioPixelcoins;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static org.bukkit.ChatColor.DARK_RED;

public class SacarItemMenu extends Menu implements Clickable, Refreshcable {
    private Player player;
    private Inventory inventory;
    private double pixelcoinsJugador;
    
    public SacarItemMenu(Player player) {
        this.player = player;
        this.pixelcoinsJugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName()).getPixelcoins();

        this.inventory = InventoryCreator.createInventoryMenu(new SacarItemInventoryFactory(pixelcoinsJugador), player.getName());
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
        ItemStack itemClickeado = event.getCurrentItem();
        if(itemClickeado == null || itemClickeado.getType().toString().equalsIgnoreCase("AIR")){
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());

        boolean inventarioLleno = espacios == 36 || (Funciones.esDeTipoItem(itemClickeado, "DIAMOND", "DIAMOND_BLOCK", "QUARTZ_BLOCK", "LAPIS_LAZULI", "LAPIS_BLOCK") && itemClickeado.getAmount() == 64);
        if(inventarioLleno){
            player.sendMessage(ChatColor.DARK_RED + "Necesitas tener el inventario con espacios libres");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        Jugador jugadorASacar = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());
        String tipoItem = itemClickeado.getType().toString();
        double cambioPixelcoins = CambioPixelcoins.getCambioTotal(tipoItem, 1); //Pixelcoins a sacar

        if(jugadorASacar.getPixelcoins() >= cambioPixelcoins){
            Funciones.enviarMensajeYSonido(player, DARK_RED + "Necesitas tener minimo " + cambioPixelcoins + " pixelcoins para convertirlo", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        CambioPixelcoins.sacarItem(jugadorASacar, tipoItem);
        player.getInventory().addItem(new ItemStack(Material.getMaterial(tipoItem), 1));

        this.pixelcoinsJugador = pixelcoinsJugador - cambioPixelcoins;

        refresh();
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void refresh() {
         Map<String, Menu> copyOfMenus = MenuManager.getCopyOfAllMenus();

         copyOfMenus.forEach( (jugador, menu) -> {
             if(jugador.equalsIgnoreCase(player.getName()) && menu instanceof Refreshcable){
                 Inventory newInveotory = InventoryCreator.createInventoryMenu(new SacarItemInventoryFactory(pixelcoinsJugador), jugador);

                 ((Refreshcable) menu).setInventory(newInveotory);
                 MenuManager.nuevoMenu(jugador, menu);
                 MenuManager.getByPlayer(jugador).openMenu();
             }
         });
    }
}
