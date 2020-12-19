package es.serversurvival.menus.menus;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.inventoryFactory.inventories.IndicesInventoryFactory;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Oferta;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.OfertaInventoryFactory;
import es.serversurvival.mySQL.Ofertas;
import es.serversurvival.mySQL.Transacciones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class OfertasMenu extends Menu implements Clickable, Paginated, RefreshcableOnPaginated {
    private OfertaInventoryFactory inventoryFactory = new OfertaInventoryFactory();

    private Inventory inventory;
    private Player player;
    private int currentIndex;
    private List<Page> pages;

    public OfertasMenu (Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(inventoryFactory, player.getName());
        this.player = player;
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        pages.add(new Page(0, inventory));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemClicked = event.getCurrentItem();

        if(itemClicked == null || Funciones.esDeTipoItem(itemClicked, "AIR")){
            return;
        }

        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno :v");
            return;
        }

        int id;
        try{
            id = Integer.parseInt(itemClicked.getItemMeta().getLore().get(2));
        }catch (Exception e) {
            return;
        }

        String itemClickckedDisplayName = itemClicked.getItemMeta().getDisplayName();

        MySQL.conectar();
        if (itemClickckedDisplayName.equalsIgnoreCase(Ofertas.NOMBRE_ITEM_RETIRAR)) {
            ofertasMySQL.retirarOferta(player, id);
        } else {
            transaccionesMySQL.realizarVenta(player.getName(), id, player);
        }
        MySQL.desconectar();

        refresh();

        event.setCancelled(true);
    }

    @Override
    public void setFactory(InventoryFactory factory) {
        this.inventoryFactory = (OfertaInventoryFactory) factory;
    }

    @Override
    public InventoryFactory getInventoryFactory() {
        return inventoryFactory;
    }

    @Override
    public void refresh() {
        OfertaInventoryFactory factory = new OfertaInventoryFactory();
        Map<String, Menu> copyOfMenus = MenuManager.getCopyOfAllMenus();

        copyOfMenus.forEach( (jugador, menu) -> {
            if(menu instanceof OfertasMenu){
                OfertasMenu menuOfertas = (OfertasMenu) menu;
                int currentIndexOfPage = menuOfertas.getCurrentIndex();

                OfertaInventoryFactory newInventoryFactory = new OfertaInventoryFactory();
                menuOfertas.setFactory(newInventoryFactory);

                List<Page> copyOfPages = new ArrayList<>(menuOfertas.getPages());
                List<Page> updatadPages = new ArrayList<>();
                updatadPages.add(new Page(0, InventoryCreator.createInventoryMenu(newInventoryFactory, menuOfertas.getPlayer().getName())));

                for(int i = 0; i < copyOfPages.size(); i++){
                    if(i == 0) continue;

                    updatadPages.add(new Page(i, newInventoryFactory.buildInventoryExecess()));
                }

                menuOfertas.setInventory(updatadPages.get(currentIndexOfPage).inventory);
                menuOfertas.setPages(updatadPages);

                menuOfertas.openMenu();
            }
        });
    }

    @Override
    public void goFordward() {
        if(throwsException()){
            Inventory newInventory = inventoryFactory.buildInventoryExecess();
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
        this.inventory = pages.get(currentIndex - 1).inventory;
        currentIndex--;

        openMenu();
    }

    @Override
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setPages(List<Page> pages) {
        this.pages = pages;
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
        return Paginated.ITEM_NAME_GOBACK;
    }

    @Override
    public String getNameItemGoFordward() {
        return Paginated.ITEM_NAME_GOFORDWARD;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
