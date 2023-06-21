package es.serversurvival.minecraftserver._shared;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado.comprar.ComprarOfertaParametros;
import es.serversurvival.pixelcoins.mercado.comprar.ComprarOfertaUseCase;
import es.serversurvival.pixelcoins.mercado.retirar.RetirarOfertaParametros;
import es.serversurvival.pixelcoins.mercado.retirar.RetirarOfertaUseCase;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.BOLD;

@RequiredArgsConstructor
public abstract class VerOfertasMercadoMenu<T extends Oferta> extends Menu {
    public final static String PROPIETARIO_OFERTA_ITEM_DISPLAYNAME = RED + "" + BOLD + UNDERLINE + "RETIRAR";
    public final static String NO_PROPIETARIO_OFERTA_DISPLAYNAME = AQUA + "" + BOLD + UNDERLINE + "COMPRAR";

    private final SyncMenuService syncMenuService;
    private final OfertasService ofertasService;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    @Override
    public final int[][] items() {
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
    public final MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(titulo())
                .fixedItems()
                .item(1, buildItemInfo())
                .items(2, buildItemsOfertas(), this::onItemTiendaOfertaClicked)
                .breakpoint(7, MenuItems.GO_BACK_PERFIL, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .sync(SyncMenuConfiguration.builder()
                        .mapper(this::mapSyncedOfertaItem)
                        .lockOnSync(true)
                        .build())
                .build();
    }

    public abstract TipoOferta[] tipoOfertas();
    public abstract Class<T> ofertaClass();
    public abstract String titulo();
    public abstract List<String> loreItemInfo();
    public abstract ItemStack buildItemFromOferta(T oferta);
    public abstract String mensajeCompraExsitosaAlComprador(T oferta, ItemStack item);
    public abstract String mensajeCompraExsitosaAlVendedor(T oferta, ItemStack item, String comprador);
    public abstract String mensajeRetiradoExistoso(T oferta, ItemStack item);

    private void onItemTiendaOfertaClicked(Player player, InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        boolean propietario = player.getUniqueId().equals(MinecraftUtils.getLastLineOfLore(item, 1));
        T oferta = ofertasService.getById(MinecraftUtils.getLastLineOfLore(item, 0), ofertaClass());

        if(propietario) {
            retirarOferta(oferta);

            event.getWhoClicked().sendMessage(mensajeRetiradoExistoso(oferta, item));
        }else{
            comprarOferta(oferta, event.getCurrentItem());

            event.getWhoClicked().sendMessage(mensajeCompraExsitosaAlComprador(oferta, item));
            enviarMensajeYSonido(oferta.getVendedorId(), mensajeCompraExsitosaAlVendedor(oferta, item, player.getName()), Sound.ENTITY_PLAYER_LEVELUP);
        }
    }

    private void comprarOferta(Oferta oferta, ItemStack itemOfertaComprado) {
        int cantidadItem = itemOfertaComprado.getAmount();

        useCaseBus.handle(ComprarOfertaParametros.of(getPlayer().getUniqueId(), oferta.getOfertaId()));

        if(cantidadItem > 1){
            itemOfertaComprado.setAmount(cantidadItem - 1);
            this.syncMenuService.sync(this);
        }else{
            reloadTodoElMenu();
        }
    }

    private void retirarOferta(Oferta oferta) {
        useCaseBus.handle(RetirarOfertaParametros.of(getPlayer().getUniqueId(), oferta.getOfertaId()));
        reloadTodoElMenu();
    }

    private void reloadTodoElMenu() {
        this.syncMenuService.sync(menuService.open(getPlayer(), this.getClass()));
    }

    private List<ItemStack> buildItemsOfertas() {
        return ofertasService.findByTipo(ofertaClass(), tipoOfertas()).stream()
                .map(this::buildItemAndSetDisplayNameAndLoreOfertaInfo)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemAndSetDisplayNameAndLoreOfertaInfo(T oferta) {
        ItemStack item = buildItemFromOferta(oferta);

        ItemUtils.setDisplayname(item, oferta.getVendedorId().equals(getPlayer().getUniqueId()) ?
                PROPIETARIO_OFERTA_ITEM_DISPLAYNAME : NO_PROPIETARIO_OFERTA_DISPLAYNAME);
        List<String> lore = getLore(item);
        lore.add("  ");
        lore.add(getPlayer().getUniqueId().toString());
        lore.add(oferta.getOfertaId().toString());
        MinecraftUtils.setLore(item, lore);

        return item;
    }

    private List<String> getLore(ItemStack item) {
        return item.getItemMeta().getLore() != null ? item.getItemMeta().getLore() : new ArrayList<>();
    }

    private ItemStack mapSyncedOfertaItem(ItemStack item, Integer itemNum) {
        boolean perteneceItemEnLaTienda = itemNum == 2;

        return perteneceItemEnLaTienda ? cambiarDisplayNameItem(item) : item;
    }

    private ItemStack cambiarDisplayNameItem(ItemStack item) {
        boolean propietarioItem = MinecraftUtils.getLastLineOfLore(item, 2).equals(getPlayer().getUniqueId());

        return ItemUtils.setDisplayname(item, propietarioItem ? PROPIETARIO_OFERTA_ITEM_DISPLAYNAME : NO_PROPIETARIO_OFERTA_DISPLAYNAME);
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER).title(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO").lore(loreItemInfo()).build();
    }
}
