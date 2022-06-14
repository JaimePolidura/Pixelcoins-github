package es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.menubuilder.MenuBuilderService;
import es.jaimetruman.menus.modules.pagination.PaginationConfiguration;
import es.jaimetruman.menus.modules.sync.SyncMenuConfiguration;
import es.jaimetruman.menus.modules.sync.SyncMenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.cancelarofertaccionserver.CancelarOfertaAccionServerUseCase;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver.ComprarAccionesServerConfirmacion;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival.tienda.vertienda.menu.TiendaMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.*;

public final class VerOfertasAccionesServerMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "    MERCADO DE ACCIONES";

    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final MenuService menuService;
    private final Player player;
    private final SyncMenuService syncMenuService;

    public VerOfertasAccionesServerMenu(Player player) {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
        this.player = player;
        this.menuService = DependecyContainer.get(MenuService.class);
        this.syncMenuService = DependecyContainer.get(SyncMenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0, 0, 0, 0},
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
                .item(2, buildItemMisAcciones())
                .items(3, buildItemsOfertas(), this::onOfertaClcked)
                .breakpoint(7, RED_BANNER, this::goBackToProfileMenu)
                .sync(SyncMenuConfiguration.builder()
                        .mapper(this::onUpdate)
                        .build())
                .paginated(PaginationConfiguration.builder()
                        .backward(8, RED_WOOL)
                        .forward(9, GREEN_WOOL)
                        .build())
                .build();
    }

    private ItemStack onUpdate(ItemStack itemFromUpdate, Integer itemNum) {
        if(itemNum != 3) return itemFromUpdate;

        UUID idOferta = UUID.fromString(ItemUtils.getLore(itemFromUpdate, 9));
        OfertaAccionServer ofertaToUpdate = this.ofertasAccionesServerService.getById(idOferta);
        boolean ownsOferta = ofertaToUpdate.esTipoOfertanteJugador() && ofertaToUpdate.getNombreOfertante().equalsIgnoreCase(player.getName());

        if(ownsOferta)
            return itemFromUpdate;

        return buildItemOferta(ofertaToUpdate);
    }

    private ItemStack buildItemMisAcciones() {
        return ItemBuilder.of(ENDER_CHEST)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "VER MIS ACCIONES")
                .build();
    }

    private void onOfertaClcked(Player player, InventoryClickEvent event) {
        boolean esOwnerDeOferta = event.getCurrentItem().getType() == RED_BANNER;
        UUID ofertaId = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 9));

        if(esOwnerDeOferta){
            cancelarOferta(player, ofertaId);
        }else{
            this.menuService.open(player, new ComprarAccionesServerConfirmacion(ofertasAccionesServerService.getById(ofertaId), player));
        }
    }

    private void cancelarOferta(Player player, UUID ofertaId) {
        (new CancelarOfertaAccionServerUseCase()).cancelar(player.getName(), ofertaId);
        enviarMensajeYSonido(player, GOLD + "Has cancelado tu oferta en el mercado. Para ver tu cartera " + AQUA + "/empresas misacciones",
                Sound.ENTITY_PLAYER_LEVELUP);

        this.reloadMenu();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new PerfilMenu(player.getName()));
    }

    private List<ItemStack> buildItemsOfertas() {
        return this.ofertasAccionesServerService.findAll().stream()
                .map(this::buildItemOferta)
                .toList();
    }

    private ItemStack buildItemOferta(OfertaAccionServer oferta) {
        boolean esOwerDeOferta = oferta.getNombreOfertante().equalsIgnoreCase(this.player.getName());

        return ItemBuilder.of(esOwerDeOferta ? RED_BANNER : BLUE_BANNER)
                .title(esOwerDeOferta ? RED + "" + UNDERLINE + "" + BOLD + "CLICK PARA CANCELAR" :
                        GOLD + "" + UNDERLINE + "" + BOLD + "CLICK PARA COMPRAR")
                .lore(List.of(
                        "   ",
                        GOLD + "Empresa: " + oferta.getEmpresa(),
                        GOLD + "Ofertante: " + oferta.getNombreOfertante() + " ("+oferta.getTipoOfertante().toString().toLowerCase()+")",
                        GOLD + "Acciones: " + oferta.getCantidad(),
                        GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC",
                        GOLD + "Precio total: " + GREEN + FORMATEA.format(oferta.getPrecio() * oferta.getCantidad()) + " PC",
                        "   ",
                        GOLD + "Fecha: " + oferta.getFecha(),
                        "  ",
                        "" + oferta.getOfertaAccionServerId()
                ))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Las empresas del servidor pueden salir",
                        "a bosla. /empresas sacarbolsa",
                        "Aqui es donde se pueden comprar y",
                        "vender las cantidad de las empresas"
                ))
                .build();
    }

    private void reloadMenu() {
        VerOfertasAccionesServerMenu newMenu = new VerOfertasAccionesServerMenu(player);
        this.menuService.open(player, newMenu);

        this.syncMenuService.sync(newMenu);
    }
}
