package es.serversurvival.menus.menus;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.SacarItemInventoryFactory;
import es.serversurvival.mySQL.enums.CambioPixelcoins;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static es.serversurvival.util.Funciones.*;

public class SacarItemMenu extends Menu implements Clickable, Refreshcable {
    private Player player;
    private Inventory inventory;
    private double pixelcoinsJugador;

    public SacarItemMenu (Player player) {
        this.player = player;
        this.pixelcoinsJugador = jugadoresMySQL.getJugador(player.getName()).getPixelcoins();

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
        int espacios = getEspaciosOcupados(player.getInventory());

        boolean inventarioLleno = espacios == 36 || (esDeTipoItem(itemClickeado, "DIAMOND", "DIAMOND_BLOCK", "QUARTZ_BLOCK", "LAPIS_LAZULI", "LAPIS_BLOCK") && itemClickeado.getAmount() == 64);
        if(inventarioLleno){
            player.sendMessage(ChatColor.DARK_RED + "Necesitas tener el inventario con espacios libres");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        Jugador jugadorASacar = jugadoresMySQL.getJugador(player.getName());
        String tipoItem = itemClickeado.getType().toString();

        double pixelcoinsSacadas = CambioPixelcoins.sacarItem(jugadorASacar, tipoItem);
        this.pixelcoinsJugador = pixelcoinsJugador - pixelcoinsSacadas;

        ScoreBoardManager.getInstance().updateScoreboard(player);

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
