package es.serversurvival.menus.menus;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.SacarMaxItemInventoryFactory;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.mySQL.enums.CambioPixelcoins.suficientesPixelcoins;
import static es.serversurvival.util.Funciones.*;
import static org.bukkit.ChatColor.DARK_RED;

public class SacarMaxItemMenu extends Menu implements Clickable {
    private Inventory inventory;
    private Player player;

    public SacarMaxItemMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new SacarMaxItemInventoryFactory(), player.getName());
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
        if(noEsDeTipoItem(itemClickeado, "DIAMOND_BLOCK", "QUARTZ_BLOCK", "LAPIS_LAZULI")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String tipoItem = itemClickeado.getType().toString();
        int espacios = getEspaciosOcupados(player.getInventory());
        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario libre");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }
        if (!suficientesPixelcoins(tipoItem, 1, jugador.getPixelcoins())) {
            enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        String tipoItemClickeado = itemClickeado.getType().toString();
        transaccionesMySQL.sacarMaxItem(tipoItemClickeado, jugador);
    }
}
