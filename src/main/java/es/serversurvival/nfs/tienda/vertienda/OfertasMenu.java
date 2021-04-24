package es.serversurvival.nfs.tienda.vertienda;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.MenuManager;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.legacy.menus.menus.Clickable;
import es.serversurvival.legacy.menus.menus.Paginated;
import es.serversurvival.legacy.menus.menus.RefreshcableOnPaginated;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.Ofertas;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import es.serversurvival.legacy.mySQL.tablasObjetos.Oferta;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.DARK_RED;

public class OfertasMenu extends Menu implements Clickable, Paginated, RefreshcableOnPaginated {
    private OfertaInventoryFactory inventoryFactory = new OfertaInventoryFactory();

    private Inventory inventory;
    private Player player;
    private int currentIndex;
    private List<Page> pages;

    public OfertasMenu(Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(inventoryFactory, player.getName());
        this.player = player;
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        pages.add(new Page(0, inventory));

        openMenu();
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemClicked = event.getCurrentItem();

        if(itemClicked == null || Funciones.esDeTipoItem(itemClicked, "AIR")){
            return;
        }

        performClick(itemClicked);

        refresh();

        event.setCancelled(true);
    }

    private void performClick (ItemStack itemClicked) {
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno :v");
            return;
        }
        Try<Integer> idTry =  Try.of(() -> Integer.parseInt(itemClicked.getItemMeta().getLore().get(2)));
        if(idTry.isFailure()){
            return;
        }
        int id = idTry.get();
        Jugador jugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());
        Oferta ofertaAComprar = AllMySQLTablesInstances.ofertasMySQL.getOferta(id);

        if (jugador.getPixelcoins() < ofertaAComprar.getPrecio()) {
            player.sendMessage(DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        String itemClickckedDisplayName = itemClicked.getItemMeta().getDisplayName();

        if (itemClickckedDisplayName.equalsIgnoreCase(Ofertas.NOMBRE_ITEM_RETIRAR)) {
            AllMySQLTablesInstances.ofertasMySQL.retirarOferta(player, id);
        } else {
            AllMySQLTablesInstances.transaccionesMySQL.realizarVenta(player.getName(), id);
            MinecraftUtils.setLore(itemClicked, Collections.singletonList("Comprado en la tienda"));
            player.getInventory().addItem(itemClicked);
        }
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
        if(weAreInTheLastPage()){
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

    private boolean weAreInTheLastPage() {
        return this.currentIndex + 1 >= this.pages.size();
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
