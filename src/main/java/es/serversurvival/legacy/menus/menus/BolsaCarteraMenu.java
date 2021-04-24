package es.serversurvival.legacy.menus.menus;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.legacy.menus.inventoryFactory.inventories.BolsaCarteraInventoryFactory;
import es.serversurvival.legacy.menus.menus.confirmaciones.BolsaVenderAccionEmpresaMenu;
import es.serversurvival.legacy.menus.menus.confirmaciones.VenderAccionesConfirmacion;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.enums.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.bukkit.Material.*;

public class BolsaCarteraMenu extends Menu implements Clickable, Paginated {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + " TU CARTERA DE ACCIONES";
    private BolsaCarteraInventoryFactory bolsaCarteraInventoryFactory = new BolsaCarteraInventoryFactory();
    private Player player;
    private Inventory inventory;
    private int currentIndex;
    private List<Page> pages;

    public BolsaCarteraMenu(Player jugador) {
        this.player = jugador;
        this.inventory = InventoryCreator.createInventoryMenu(bolsaCarteraInventoryFactory, jugador.getName());
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        this.pages.add(new Page(0, inventory));
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
        ItemStack clikedItem = event.getCurrentItem();

        boolean notValid = clikedItem == null || !Funciones.esDeTipo(event.getCurrentItem(), BOOK, NAME_TAG, REDSTONE_TORCH, GREEN_BANNER, COAL, GOLD_BLOCK)
                || clikedItem.getItemMeta().getLore().get(1) == null;

        if(notValid){
            return;
        }

        if(Funciones.esDeTipo(clikedItem, BOOK)){
            ElegirInversionMenu menu = new ElegirInversionMenu((Player) event.getWhoClicked());
            return;
        }

        performClick(clikedItem);
    }

    private void performClick (ItemStack clikedItem) {
        Material clickedItemType = clikedItem.getType();
        TipoPosicion tipoPosicion = clickedItemType == NAME_TAG ? TipoPosicion.LARGO : TipoPosicion.CORTO;
        List<String> loreItemClicked = clikedItem.getItemMeta().getLore();
        int id = Integer.parseInt(loreItemClicked.get(loreItemClicked.size() - 1).split(" ")[1]);

        if(clickedItemType == GREEN_BANNER){
            BolsaVenderAccionEmpresaMenu menu = new BolsaVenderAccionEmpresaMenu(player, AllMySQLTablesInstances.posicionesAbiertasMySQL.getPosicionAbierta(id));
        }else{
            VenderAccionesConfirmacion confirmacion = new VenderAccionesConfirmacion(player, id, tipoPosicion, TipoActivo.ACCIONES, loreItemClicked); //TODO Hay que hacerlo compatible con los demas activos
        }
    }

    @Override
    public void goFordward() {
        if(weAreInTheLasPage()){
            Inventory newInventory = bolsaCarteraInventoryFactory.buildInventoryExecess();
            this.inventory = newInventory;
            pages.add(new Page(currentIndex + 1, newInventory));
        }else{
            Page newPage = pages.get(currentIndex + 1);
            this.inventory = newPage.inventory;
        }

        currentIndex++;
        openMenu();
    }

    private boolean weAreInTheLasPage() {
        return this.currentIndex + 1 >= this.pages.size();
    }

    @Override
    public void goBack() {
        if(currentIndex == 0){
            PerfilMenu menu = new PerfilMenu(player);
            menu.openMenu();
        }else{
            this.inventory = pages.get(currentIndex - 1).inventory;
            currentIndex--;

            openMenu();
        }
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public List<Page> getPages() {
        return pages;
    }

    @Override
    public String getNameItemGoBack() {
        return ITEM_NAME_GOBACK;
    }

    @Override
    public String getNameItemGoFordward() {
        return ITEM_NAME_GOFORDWARD;
    }
}
