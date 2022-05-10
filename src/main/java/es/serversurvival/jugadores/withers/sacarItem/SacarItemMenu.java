package es.serversurvival.jugadores.withers.sacarItem;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Refreshcable;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
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
    private final JugadoresService jugadoresService;
    private Player player;
    private Inventory inventory;
    private double pixelcoinsJugador;

    public SacarItemMenu(Player player) {
        this.player = player;
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.pixelcoinsJugador = this.jugadoresService.getByNombre(player.getName()).getPixelcoins();

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

        Jugador jugadorASacar = this.jugadoresService.getByNombre(player.getName());
        String tipoItem = itemClickeado.getType().toString();
        double cambioPixelcoins = CambioPixelcoins.getCambioTotal(tipoItem, 1); //Pixelcoins a sacar

        if(jugadorASacar.getPixelcoins() < cambioPixelcoins){
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
