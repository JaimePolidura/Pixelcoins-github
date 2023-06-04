package es.serversurvival.v2.minecraftserver.tienda.vertienda;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.v2.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.mercado.comprar.ComprarOfertaParametros;
import es.serversurvival.v2.pixelcoins.mercado.comprar.ComprarOfertaUseCase;
import es.serversurvival.v2.pixelcoins.mercado.retirar.RetirarOfertaParametros;
import es.serversurvival.v2.pixelcoins.mercado.retirar.RetirarOfertaUseCase;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

@RequiredArgsConstructor
public final class TiendaMenu extends Menu<Player> {
    public final static String OWNER_TIENDAOBJETO_ITEM_NAME = RED + "" + BOLD + "CLICK PARA RETIRAR";
    public final static String NO_OWNER_TIENDAOBJETO_ITEM_NAME = AQUA + "" + BOLD + "CLICK PARA COMPRAR";
    public final static String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";

    private final RetirarOfertaUseCase retirarOfertaUseCase;
    private final ComprarOfertaUseCase comprarOfertaUseCase;
    private final JugadoresService jugadoresService;
    private final SyncMenuService syncMenuService;
    private final OfertasService ofertasService;
    private final MenuService menuService;

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
                .items(2, buildItemTiendasObjetos(), this::onItemTiendaObjetoClicked)
                .breakpoint(7, Material.GREEN_BANNER, (p, e) -> menuService.open(p, PerfilMenu.class, p))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .sync(SyncMenuConfiguration.builder()
                        .mapper(this::mapSyncedObjetoTienda)
                        .lockOnSync(true)
                        .build())
                .build();
    }

    private void onItemTiendaObjetoClicked(Player player, InventoryClickEvent event) {
        if(getEspaciosOcupados(player.getInventory()) == 36){
            player.sendMessage(DARK_RED + "Tienes el inventario lleno");
            return;
        }

        ItemStack itemClickeado = event.getCurrentItem();
        UUID ofertaId = UUID.fromString(ItemUtils.getLore(itemClickeado, 3));
        OfertaTiendaItemMinecraft oferta = ofertasService.getById(ofertaId, OfertaTiendaItemMinecraft.class);
        boolean esPropietario = oferta.getVendedorId().equals(getState().getUniqueId());

        if(esPropietario){
            retirarTiendaObjeto(oferta);
        }else{
            comprarTiendaObjeto(oferta, event);
        }
    }

    private void comprarTiendaObjeto(OfertaTiendaItemMinecraft oferta, InventoryClickEvent event) {
        ItemStack itemComprado = event.getCurrentItem();
        int cantidadItem = itemComprado.getAmount();

        comprarOfertaUseCase.comprarOferta(ComprarOfertaParametros.of(getState().getUniqueId(), oferta.getOfertaId()));

        if(cantidadItem > 1){
            itemComprado.setAmount(cantidadItem - 1);
        }else{
            reloadTodoElMenu();
        }

        event.getWhoClicked().sendMessage(GOLD + "Has comprado " + itemComprado.getType() + " por " + GREEN + "" +
                FORMATEA.format(oferta.getPrecio()) + "PC");
        enviarMensajeYSonido(oferta.getVendedorId(), GOLD + event.getWhoClicked().getName() + " te ha comprado 1 de " + itemComprado.getType() +
                " por " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    private void reloadTodoElMenu() {
        this.syncMenuService.sync(menuService.open(getState(), TiendaMenu.class, getState()));
    }

    private void retirarTiendaObjeto(OfertaTiendaItemMinecraft oferta) {
        retirarOfertaUseCase.retirarOfertaUseCase(RetirarOfertaParametros.of(getState().getUniqueId(), oferta.getOfertaId()));
        reloadTodoElMenu();
    }

    private List<ItemStack> buildItemTiendasObjetos() {
        return this.ofertasService.findByTipo(TipoOferta.TIENDA_ITEM_MINECRAFT, OfertaTiendaItemMinecraft.class).stream()
                .map(this::ofertaToMinecraftObjeto)
                .collect(Collectors.toList());
    }

    private ItemStack ofertaToMinecraftObjeto(OfertaTiendaItemMinecraft objetoTienda) {
        ItemStack itemStack = objetoTienda.toItemStack();

        String vendedorNombre = jugadoresService.getNombreById(objetoTienda.getVendedorId());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + FORMATEA.format(objetoTienda.getPrecio()) + " PC");
        lore.add(ChatColor.GOLD + "Venderdor: " + vendedorNombre);
        lore.add("   ");
        lore.add("" + objetoTienda.getOfertaId());

        String displayName = objetoTienda.getVendedorId().equals(getState().getUniqueId()) ?
                OWNER_TIENDAOBJETO_ITEM_NAME :
                NO_OWNER_TIENDAOBJETO_ITEM_NAME;

        setLoreAndDisplayName(itemStack, lore, displayName);

        return itemStack;
    }

    private ItemStack mapSyncedObjetoTienda(ItemStack item, Integer itemNum) {
        boolean perteneceItemEnLaTienda = itemNum == 2;

        return perteneceItemEnLaTienda ? cambiarDisplayNameItem(item) : item;
    }

    private ItemStack cambiarDisplayNameItem(ItemStack item) {
        boolean propietarioItem = ItemUtils.getLore(item, 1).split(" ")[1].equalsIgnoreCase(getState().getName());

        return ItemUtils.setDisplayname(item, propietarioItem ? OWNER_TIENDAOBJETO_ITEM_NAME : NO_OWNER_TIENDAOBJETO_ITEM_NAME);
    }

    private ItemStack buildItemInfo() {
        List<String> lore = new ArrayList<>();
        lore.add("Para vender un objeto: 1) selecciona");
        lore.add("el item con la mano y 2) pon el comando:");
        lore.add("/tienda vender <precio>");

        return ItemBuilder.of(Material.PAPER).title(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO").lore(lore).build();
    }
}
