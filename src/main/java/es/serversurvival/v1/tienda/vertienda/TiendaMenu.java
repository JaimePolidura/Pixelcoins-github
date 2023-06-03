package es.serversurvival.v1.tienda.vertienda;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1._shared.utils.ItemsUtils;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.perfil.PerfilMenu;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import es.serversurvival.v1.tienda._shared.application.TiendaService;
import es.serversurvival.v1.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.v1.tienda.comprar.ComprarTiendaObjetoUseCase;
import es.serversurvival.v1.tienda.retirar.RetirarOfertaUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class TiendaMenu extends Menu<Object> implements AfterShow {
    public final static String OWNER_TIENDAOBJETO_ITEM_NAME = RED + "" + BOLD + "CLICK PARA RETIRAR";
    public final static String NO_OWNER_TIENDAOBJETO_ITEM_NAME = AQUA + "" + BOLD + "CLICK PARA COMPRAR";
    public final static String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";

    private final ComprarTiendaObjetoUseCase comprarTiendaObjetoUseCase;
    private final RetirarOfertaUseCase retirarOfertaUseCase;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final SyncMenuService syncMenuService;
    private final TiendaService tiendaService;
    private final MenuService menuService;

    private String jugadorNombre;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .item(1, buildItemInfo())
                .items(2, buildItemTiendas(), this::onItemTiendaOnClick)
                .breakpoint(7, Material.GREEN_BANNER, (p,e) -> this.menuService.open(p, PerfilMenu.class, jugadoresService.getByNombre(p.getName())))
                .sync(SyncMenuConfiguration.builder()
                        .mapper(this::onSyncItemMap)
                        .build())
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void onItemTiendaOnClick(Player playerWhoClicked, InventoryClickEvent event) {
        int espacios = Funciones.getEspaciosOcupados(playerWhoClicked.getInventory());
        if(espacios == 36){
            playerWhoClicked.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno :v");
            return;
        }

        UUID itemTiendaId = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 3));
        Jugador jugadorWhoClicked = this.jugadoresService.getByNombre(playerWhoClicked.getName());
        TiendaObjeto ofertaAComprar = this.tiendaService.getById(itemTiendaId);

        if (ofertaAComprar.getJugador().equalsIgnoreCase(jugadorWhoClicked.getNombre()))
            retirarItemTienda(playerWhoClicked, itemTiendaId, jugadorWhoClicked, event.getSlot());
        else
            comprarItemTiendaObjeto(playerWhoClicked, jugadorWhoClicked, ofertaAComprar, event.getSlot());
    }

    private void comprarItemTiendaObjeto(Player playerWhoClicked, Jugador jugadorWhoClicked, TiendaObjeto tiendaObjeto, int slotItem) {
        if (jugadorWhoClicked.getPixelcoins() < tiendaObjeto.getPrecio()) {
            playerWhoClicked.sendMessage(DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        ItemStack itemComprado = this.comprarTiendaObjetoUseCase.realizarVenta(playerWhoClicked.getName(),
                tiendaObjeto.getTiendaObjetoId());

        if(tiendaObjeto.getCantidad() == 1)
            borrarItem();
        else
            decrementarCantidadItem(slotItem);

        MinecraftUtils.setLore(itemComprado, Collections.singletonList("Comprado en la tienda"));
        playerWhoClicked.getInventory().addItem(itemComprado);
        playerWhoClicked.sendMessage(GOLD + "Has comprado " + itemComprado.getType() + " por " + GREEN + "" +
                FORMATEA.format(tiendaObjeto.getPrecio()) + "PC");

        String mensajeOnlineToVendedor = GOLD + playerWhoClicked.getName() + " te ha comprado 1 de " + itemComprado.getType() +
                " por " + GREEN + FORMATEA.format(tiendaObjeto.getPrecio()) + " PC";
        String mensajeOfflineVendedor = playerWhoClicked.getName() + " te ha comprado 1 de " + itemComprado.getType() +
                " por " + FORMATEA.format(tiendaObjeto.getPrecio()) + " PC";

        enviadorMensajes.enviarMensaje(tiendaObjeto.getJugador(), mensajeOnlineToVendedor, mensajeOfflineVendedor,
                Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void borrarItem() {
        reloadMenu();
    }

    private void decrementarCantidadItem(int slotItem) {
        ItemStack itemComprado = super.getActualPage().getInventory().getItem(slotItem);
        itemComprado.setAmount(itemComprado.getAmount() - 1);

        this.syncMenuService.sync(this);
    }

    private void retirarItemTienda(Player player, UUID itemTiendaId, Jugador jugador, int itemSlot) {
        var item = this.retirarOfertaUseCase.retirarOferta(jugador.getNombre(), itemTiendaId);

        player.getInventory().addItem(item);

        reloadMenu();
    }

    private List<ItemStack> buildItemTiendas() {
        return this.tiendaService.findAll().stream()
                .map(this::buildItemTienda)
                .toList();
    }

    private ItemStack buildItemTienda(TiendaObjeto itemTienda) {
        ItemStack itemStackAInsertar = ItemsUtils.getItemStakcByTiendaObjeto(itemTienda);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + FORMATEA.format(itemTienda.getPrecio()) + " PC");
        lore.add(ChatColor.GOLD + "Venderdor: " + itemTienda.getJugador());
        lore.add("   ");
        lore.add("" + itemTienda.getTiendaObjetoId());

        String displayName = itemTienda.getJugador().equalsIgnoreCase(jugadorNombre) ?
                OWNER_TIENDAOBJETO_ITEM_NAME :
                NO_OWNER_TIENDAOBJETO_ITEM_NAME;

        MinecraftUtils.setLoreAndDisplayName(itemStackAInsertar, lore, displayName);

        return itemStackAInsertar;
    }

    private ItemStack buildItemInfo() {
        List<String> lore = new ArrayList<>();
        lore.add("Para vender un objeto: 1) selecciona");
        lore.add("el item con la mano y 2) pon el comando:");
        lore.add("/vender <precio>");

        return ItemBuilder.of(Material.PAPER).title(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO").lore(lore).build();
    }

    private ItemStack onSyncItemMap(ItemStack item, Integer itemNum) {
        boolean isItemTienda = itemNum == 2;

        return isItemTienda ? modifyItemTienda(item) : item;
    }

    private ItemStack modifyItemTienda(ItemStack item) {
        boolean ownsItemTienda = ItemUtils.getLore(item, 1).split(" ")[1].equalsIgnoreCase(this.jugadorNombre);

        return ItemUtils.setDisplayname(item, ownsItemTienda ? OWNER_TIENDAOBJETO_ITEM_NAME : NO_OWNER_TIENDAOBJETO_ITEM_NAME);
    }

    private void reloadMenu() {
        var newMenu = this.menuService.open(Bukkit.getPlayer(jugadorNombre), TiendaMenu.class);

        this.syncMenuService.sync(newMenu);
    }

    @Override
    public void afterShow(Player player) {
        this.jugadorNombre = player.getName();
    }
}
