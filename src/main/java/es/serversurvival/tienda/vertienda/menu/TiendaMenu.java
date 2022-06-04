package es.serversurvival.tienda.vertienda.menu;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.messaging.MessagingConfiguration;
import es.jaimetruman.menus.modules.messaging.MessagingMenuService;
import es.jaimetruman.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.ItemsUtils;
import es.serversurvival._shared.utils.MinecraftUtils;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.tienda.comprar.ComprarTiendaObjetoUseCase;
import es.serversurvival.tienda.retirar.RetirarOfertaUseCase;
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

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class TiendaMenu extends Menu {
    public final static String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";

    private final MessagingMenuService messagingMenuService;
    private final String jugador;
    private final MenuService menuService;
    private final TiendaService tiendaService;
    private final JugadoresService jugadoresService;

    public TiendaMenu(String jugador) {
        this.messagingMenuService = DependecyContainer.get(MessagingMenuService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
        this.tiendaService = DependecyContainer.get(TiendaService.class);
        this.jugador = jugador;
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

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
                .breakpoint(7, Material.GREEN_BANNER, (p,e) -> this.menuService.open(p, new PerfilMenu(p.getName())))
                .messaging(MessagingConfiguration.builder()
                        .onMessage(ItemParteCompradaTiendaMenuMessage.class, this::onItemCompradoTienda)
                        .onMessage(ItemNuevoTiendaMenuMessage.class, this::onItemNuevoTienda)
                        .onMessage(ItemRetiradoTiendaMenuMessage.class, this::onItemRetirado)
                        .onMessage(ItemTodoCompradoTiendaMenuMessage.class, this::onItemTodoComprado)
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

        ItemStack itemComprado = ComprarTiendaObjetoUseCase.INSTANCE.realizarVenta(playerWhoClicked.getName(),
                tiendaObjeto.getTiendaObjetoId());

        if(tiendaObjeto.getCantidad() == 1)
            borrarItem(slotItem);
        else
            decrementarCantidadItem(slotItem, tiendaObjeto);

        MinecraftUtils.setLore(itemComprado, Collections.singletonList("Comprado en la tienda"));
        playerWhoClicked.getInventory().addItem(itemComprado);
        playerWhoClicked.sendMessage(GOLD + "Has comprado " + itemComprado.getType() + " por " + GREEN + "" +
                FORMATEA.format(tiendaObjeto.getPrecio()) + "PC");

        String mensajeOnlineToVendedor = GOLD + playerWhoClicked.getName() + " te ha comprado 1 de " + itemComprado.getType() +
                " por " + GREEN + FORMATEA.format(tiendaObjeto.getPrecio()) + " PC";
        String mensajeOfflineVendedor = playerWhoClicked.getName() + " te ha comprado 1 de " + itemComprado.getType() +
                " por " + FORMATEA.format(tiendaObjeto.getPrecio()) + " PC";

        Funciones.enviarMensaje(tiendaObjeto.getJugador(), mensajeOnlineToVendedor, mensajeOfflineVendedor,
                Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void borrarItem(int slotItem) {
        this.getActualPage().getInventory().clear(slotItem);
        this.messagingMenuService.broadCastMessage(this, new ItemTodoCompradoTiendaMenuMessage(
                this.getActualPageNumber(), slotItem
        ));
    }

    private void decrementarCantidadItem(int slotItem, TiendaObjeto tiendaObjeto) {
        ItemStack itemComprado = super.getActualPage().getInventory().getItem(slotItem);

        itemComprado.setAmount(itemComprado.getAmount() - 1);
        this.messagingMenuService.broadCastMessage(this, new ItemParteCompradaTiendaMenuMessage(
                tiendaObjeto, slotItem, super.getActualPageNumber()
        ));
    }

    private void retirarItemTienda(Player player, UUID itemTiendaId, Jugador jugador, int itemSlot) {
        ItemStack itemRetirado = RetirarOfertaUseCase.INSTANCE.retirarOferta(jugador.getNombre(), itemTiendaId);
        player.getInventory().addItem(itemRetirado);
        super.getActualPage().getInventory().clear(itemSlot);

        player.sendMessage(GOLD + "Has retirado " + itemRetirado.getType() + " de la tienda");

        this.messagingMenuService.broadCastMessage(this, new ItemRetiradoTiendaMenuMessage(
                this.getActualPageNumber(), itemSlot
        ));
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

        String displayName = itemTienda.getJugador().equalsIgnoreCase(jugador) ?
                ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR" :
                ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";

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

    private void onItemTodoComprado(ItemTodoCompradoTiendaMenuMessage msg) {
        this.getPages().get(msg.getPageNumber())
                .getInventory()
                .clear(msg.getSlotItem());
    }

    private void onItemRetirado(ItemRetiradoTiendaMenuMessage message) {
        this.getPages().get(message.getPageNumber())
                .getInventory()
                .clear(message.getSlotItem());
    }

    private void onItemNuevoTienda(ItemNuevoTiendaMenuMessage message) {
        ItemStack itemToAdd = this.buildItemTienda(message.getTiendaObjeto());
        super.getPages().get(super.getPages().size() - 1).getInventory().addItem(itemToAdd);
    }

    private void onItemCompradoTienda(ItemParteCompradaTiendaMenuMessage itemComprado) {
        this.getPages().get(itemComprado.getPageNumber())
                .getInventory()
                .getItem(itemComprado.getSlot())
                .setAmount(itemComprado.getTiendaObjeto().getCantidad());
    }
}
