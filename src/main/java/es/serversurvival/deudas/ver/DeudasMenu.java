package es.serversurvival.deudas.ver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.mysql.Deudas;
import es.serversurvival.deudas._shared.newformat.application.DeudasService;
import es.serversurvival.deudas.cancelar.CancelarDeudaUseCase;
import es.serversurvival.deudas._shared.newformat.domain.Deuda;
import es.serversurvival.deudas.pagarTodo.PagarDeudaCompletaUseCase;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.CanGoBack;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Refreshcable;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

public class DeudasMenu extends Menu implements Clickable, Refreshcable, CanGoBack {
    private final PagarDeudaCompletaUseCase pagarDeudaUseCase;
    private final CancelarDeudaUseCase cancelarDeudaUseCase;
    private final DeudasService deudasService;

    public final static String tiulo = DARK_RED + "" + BOLD + "          TUS DEUDAS";
    private Player player;
    private Inventory inventory;

    public DeudasMenu (Player player) {
        this.pagarDeudaUseCase = new PagarDeudaCompletaUseCase();
        this.cancelarDeudaUseCase = new CancelarDeudaUseCase();
        this.deudasService = DependecyContainer.get(DeudasService.class);

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
        UUID deudaId = UUID.fromString(String.valueOf(itemCliqueado.getItemMeta().getLore().get(4).split(" ")[1]));
        String tipoItem = itemCliqueado.getType().toString();

        if(tipoItem.equalsIgnoreCase("RED_BANNER")){
            pagarDeuda(deudaId);
        }else{
            cancelarDeuda(deudaId);
        }

    }

    private void pagarDeuda (UUID deudaId) {
        Deuda deudaAPagar = this.deudasService.getDeudaById(deudaId);

        Player jugadorQueVaAPagar = Bukkit.getPlayer(deudaAPagar.getDeudor());
        double pixelcoinsAPagar = deudaAPagar.getPixelcoins_restantes();

        if(AllMySQLTablesInstances.jugadoresMySQL.getJugador(jugadorQueVaAPagar.getName()).getPixelcoins() < pixelcoinsAPagar){
            jugadorQueVaAPagar.sendMessage(DARK_RED + "No tienes el suficiente dinero");
            jugadorQueVaAPagar.playSound(jugadorQueVaAPagar.getLocation(), ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        Deuda deudaPagada = pagarDeudaUseCase.pagarDeuda(deudaId, jugadorQueVaAPagar.getName());

        Funciones.enviarMensajeYSonido(player, GOLD + "Has pagado a " + deudaPagada.getAcredor() + " toda la deuda: "
                + GREEN + formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC", ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = GOLD + player.getName() + " ta ha pagado toda la deuda: " + GREEN +
                formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC";

        String mensajeOffline = player.getName() + " ta ha pagado toda la deuda: " + deudaPagada.getPixelcoins_restantes() + " PC";
        Funciones.enviarMensaje(deudaPagada.getAcredor(), mensajeOnline, mensajeOffline, ENTITY_PLAYER_LEVELUP, 10, 1);

        refresh();
    }

    private void cancelarDeuda (UUID deudaId) {
        Deuda deudaCancelada = cancelarDeudaUseCase.cancelarDeuda(player.getName(), deudaId);

        Funciones.enviarMensajeYSonido(player, GOLD + "Has cancelado la deuda a " + deudaCancelada.getDeudor() + "!", ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = GOLD + player.getName() + " te ha cancelado la deuda de " + GREEN +
                formatea.format(deudaCancelada.getPixelcoins_restantes()) + " PC";
        Funciones.enviarMensaje(deudaCancelada.getDeudor(), mensajeOnline, mensajeOnline, ENTITY_PLAYER_LEVELUP, 10, 1);

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
