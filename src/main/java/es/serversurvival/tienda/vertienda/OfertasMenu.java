package es.serversurvival.tienda.vertienda;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda.comprar.ObjetoTiendaComprado;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.menus.Paginated;
import es.serversurvival._shared.menus.RefreshcableOnPaginated;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.tienda.comprar.ComprarOfertaUseCase;
import es.serversurvival.tienda.retirar.RetirarOfertaUseCase;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.MinecraftUtils;
import io.vavr.control.Try;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public class OfertasMenu extends Menu implements Clickable, Paginated, RefreshcableOnPaginated {
    private final JugadoresService jugadoresService;
    private final TiendaService tiendaService;
    private final ComprarOfertaUseCase comprarOfertaUseCase;
    private final RetirarOfertaUseCase retirarOfertaUseCase;

    private OfertaInventoryFactory inventoryFactory = new OfertaInventoryFactory();

    private final Player player;
    private Inventory inventory;
    private int currentIndex;
    private List<Page> pages;

    public OfertasMenu(Player player) {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.tiendaService = DependecyContainer.get(TiendaService.class);
        this.comprarOfertaUseCase = new ComprarOfertaUseCase();
        this.retirarOfertaUseCase = new RetirarOfertaUseCase();

        this.inventory = InventoryCreator.createInventoryMenu(inventoryFactory, player.getName());
        this.player = player;
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        pages.add(new Page(0, inventory));

        openMenu();
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
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
        Try<UUID> idTry =  Try.of(() -> UUID.fromString(itemClicked.getItemMeta().getLore().get(2)));
        if(idTry.isFailure()){
            return;
        }
        UUID id = idTry.get();
        Jugador jugador = this.jugadoresService.getByNombre(player.getName());
        TiendaObjeto ofertaAComprar = this.tiendaService.getById(id);

        if (jugador.getPixelcoins() < ofertaAComprar.getPrecio()) {
            player.sendMessage(DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        String itemClickckedDisplayName = itemClicked.getItemMeta().getDisplayName();

        if (itemClickckedDisplayName.equalsIgnoreCase(OfertaInventoryFactory.NOMBRE_ITEM_RETIRAR)) {
            ItemStack itemRetirado = retirarOfertaUseCase.retirarOferta(jugador.getNombre(), id);
            player.getInventory().addItem(itemRetirado);

            player.sendMessage(GOLD + "Has retirado " + itemRetirado.getType() + " de la tienda");
        } else {
            ItemStack itemComprado = comprarOfertaUseCase.realizarVenta(player.getName(), id);

            MinecraftUtils.setLore(itemComprado, Collections.singletonList("Comprado en la tienda"));
            player.getInventory().addItem(itemComprado);
            player.sendMessage(GOLD + "Has comprado " + itemComprado.getType());
        }
    }


    @EventListener
    public void onItemCompradoTienda (ObjetoTiendaComprado evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());

        Funciones.enviarMensajeYSonido(player, GOLD + "Has comprado: " + evento.getObjeto() + " , por " + GREEN +
                FORMATEA.format(evento.getPrecioUnidad()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Funciones.enviarMensajeYSonidoSiOnline(evento.getVendedor(), GOLD + evento.getComprador() + " te ha comprado: " +
                evento.getObjeto() + " por: " + GREEN + FORMATEA.format(evento.getPrecioUnidad()) + " PC ", Sound.ENTITY_PLAYER_LEVELUP);
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
        Map<String, Menu> copyOfMenus = MenuManager.getCopyOfAllMenus();

        copyOfMenus.forEach( (jugador, menu) -> {
            if(menu instanceof OfertasMenu menuOfertas){
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
