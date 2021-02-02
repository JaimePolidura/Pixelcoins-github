package es.serversurvival.menus.menus;

import es.serversurvival.menus.menus.confirmaciones.VenderAccionesConfirmacion;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.BolsaCarteraInventoryFactory;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival.mySQL.enums.TipoPosicion.*;

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
    public void onClick(InventoryClickEvent event) {
        ItemStack clikedItem = event.getCurrentItem();

        boolean notValidClick = clikedItem == null || !Funciones.cuincideNombre(clikedItem.getType().toString(), "BOOK", "NAME_TAG", "REDSTONE_TORCH", "GREEN_BANNER") || clikedItem.getItemMeta().getLore().get(1) == null;
        if(notValidClick){
            return;
        }

        String clickedItemType = clikedItem.getType().toString();
        if(Funciones.esDeTipoItem(clikedItem, "BOOK")){
            ElegirInversionMenu menu = new ElegirInversionMenu((Player) event.getWhoClicked());
            return;
        }

        performClick(clikedItem);
    }

    private void performClick (ItemStack clikedItem) {
        Material clickedItemType = clikedItem.getType();
        TipoPosicion tipoPosicion = clickedItemType == Material.NAME_TAG ? LARGO : CORTO;
        List<String> loreItemClicked = clikedItem.getItemMeta().getLore();
        int id = Integer.parseInt(loreItemClicked.get(loreItemClicked.size() - 1).split(" ")[1]);

        if(clickedItemType == Material.GREEN_BANNER){ //Accion del server
            VenderAccionesConfirmacion confirmacion = new VenderAccionesConfirmacion(player, id, tipoPosicion, TipoActivo.ACCIONES_SERVER, loreItemClicked);
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
