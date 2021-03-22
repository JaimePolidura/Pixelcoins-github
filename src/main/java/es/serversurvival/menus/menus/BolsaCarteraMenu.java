package es.serversurvival.menus.menus;

import es.serversurvival.menus.menus.confirmaciones.VenderAccionesConfirmacion;
import es.serversurvival.mySQL.enums.POSICION;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.BolsaCarteraInventoryFactory;
import es.serversurvival.mySQL.PosicionesAbiertas;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        pages.add(new Page(0, inventory));
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

        if(clikedItem == null || !Funciones.cuincideNombre(clikedItem.getType().toString(), "BOOK", "NAME_TAG", "REDSTONE_TORCH") || clikedItem.getItemMeta().getLore().get(1) == null){
            return;
        }
        String clickedItemType = clikedItem.getType().toString();

        if(clickedItemType.equalsIgnoreCase("BOOK")){
            ElegirInversionMenu menu = new ElegirInversionMenu((Player) event.getWhoClicked());
            menu.openMenu();
            return;
        }

        POSICION tipoPosicion;
        if (clickedItemType.equalsIgnoreCase("NAME_TAG")){
            tipoPosicion = POSICION.LARGO;
        }else{
            tipoPosicion = POSICION.CORTO;
        }

        List<String> lore = clikedItem.getItemMeta().getLore();
        String acciones = lore.get(5).split(" ")[2];
        String valorTotal = lore.get(10).split(" ")[2];
        String beneficios = lore.get(8).split(" ")[2];
        String rentabilidad = lore.get(9).split(" ")[1];
        int id = Integer.parseInt(lore.get(lore.size() - 1).split(" ")[1]);

        VenderAccionesConfirmacion confirmacion = new VenderAccionesConfirmacion(player, tipoPosicion, acciones, valorTotal, beneficios, rentabilidad, id);
        confirmacion.openMenu();
    }

    @Override
    public void goFordward() {
        if(throwsException()){
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

    private boolean throwsException () {
        try{
            pages.get(currentIndex + 1);
            return false;
        }catch (Exception e){
            return true;
        }
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