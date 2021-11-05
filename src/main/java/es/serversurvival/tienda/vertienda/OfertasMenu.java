package es.serversurvival.tienda.vertienda;

import es.jaime.EventListener;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.tienda.comprar.ItemCompradoEvento;
import es.serversurvival.shared.menus.Menu;
import es.serversurvival.shared.menus.MenuManager;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import es.serversurvival.shared.menus.inventory.InventoryFactory;
import es.serversurvival.shared.menus.Clickable;
import es.serversurvival.shared.menus.Paginated;
import es.serversurvival.shared.menus.RefreshcableOnPaginated;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.tienda.mySQL.ofertas.Ofertas;
import es.serversurvival.tienda.mySQL.ofertas.Oferta;
import es.serversurvival.tienda.comprar.ComprarOfertaUseCase;
import es.serversurvival.tienda.retirar.RetirarOfertaUseCase;
import es.serversurvival.shared.utils.Funciones;
import es.serversurvival.shared.utils.MinecraftUtils;
import io.vavr.control.Try;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public class OfertasMenu extends Menu implements Clickable, Paginated, RefreshcableOnPaginated {
    private final ComprarOfertaUseCase comprarOfertaUseCase = ComprarOfertaUseCase.INSTANCE;
    private final RetirarOfertaUseCase retirarOfertaUseCase = RetirarOfertaUseCase.INSTANCE;

    private OfertaInventoryFactory inventoryFactory = new OfertaInventoryFactory();

    private Player player;
    private Inventory inventory;
    private int currentIndex;
    private List<Page> pages;

    public OfertasMenu () {}

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
            ItemStack itemRetirado = retirarOfertaUseCase.retirarOferta(id);
            player.getInventory().addItem(itemRetirado);

            player.sendMessage(GOLD + "Has retirado " + itemRetirado.getType().toString() + " de la tienda");
        } else {
            ItemStack itemComprado = comprarOfertaUseCase.realizarVenta(player.getName(), id);

            MinecraftUtils.setLore(itemComprado, Collections.singletonList("Comprado en la tienda"));
            player.getInventory().addItem(itemComprado);
            player.sendMessage(GOLD + "Has comprado " + itemComprado.getType().toString());
        }
    }


    @EventListener
    public void onItemCompradoTienda (ItemCompradoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());

        Funciones.enviarMensajeYSonido(player, GOLD + "Has comprado: " + evento.getObjeto() + " , por " + GREEN +
                AllMySQLTablesInstances.formatea.format(evento.getPrecioUnidad()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Funciones.enviarMensajeYSonidoSiOnline(evento.getVendedor(), GOLD + evento.getComprador() + " te ha comprado: " +
                evento.getObjeto() + " por: " + GREEN + AllMySQLTablesInstances.formatea.format(evento.getPrecioUnidad()) + " PC ", Sound.ENTITY_PLAYER_LEVELUP);
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
