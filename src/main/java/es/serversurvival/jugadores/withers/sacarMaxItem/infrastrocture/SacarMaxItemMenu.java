package es.serversurvival.jugadores.withers.sacarMaxItem.infrastrocture;

import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival.jugadores.withers.sacarMaxItem.application.SacarMaxItemUseCase;
import es.serversurvival.shared.menus.Menu;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import es.serversurvival.shared.menus.Clickable;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.DARK_RED;

public class SacarMaxItemMenu extends Menu implements Clickable {
    private final SacarMaxItemUseCase useCase;
    private Inventory inventory;
    private Player player;

    public SacarMaxItemMenu(Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new SacarMaxItemInventoryFactory(), player.getName());
        this.useCase = SacarMaxItemUseCase.INSTANCE;

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
        if(Funciones.noEsDeTipoItem(itemClickeado, "DIAMOND_BLOCK", "QUARTZ_BLOCK", "LAPIS_LAZULI")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String tipoItem = itemClickeado.getType().toString();
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        Jugador jugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());

        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario libre");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }
        if (!CambioPixelcoins.suficientesPixelcoins(tipoItem, 1, jugador.getPixelcoins())) {
            Funciones.enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        String tipoItemClickeado = itemClickeado.getType().toString();

        this.useCase.sacarMaxItem(tipoItemClickeado, jugador);
    }
}
