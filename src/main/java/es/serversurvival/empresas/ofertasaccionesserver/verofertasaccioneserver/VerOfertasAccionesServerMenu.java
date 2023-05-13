package es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuConfiguration;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.cancelarofertaccionserver.CancelarOfertaAccionServerUseCase;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver.ComprarAccionesServerConfirmacion;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.*;

@RequiredArgsConstructor
public final class VerOfertasAccionesServerMenu extends Menu<Object> implements AfterShow {
    private final CancelarOfertaAccionServerUseCase cancelarOfertaAccionServerUseCase;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final SyncMenuService syncMenuService;
    private final MenuService menuService;

    private Player player;

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
                .title(DARK_RED + "" + BOLD + "    MERCADO DE ACCIONES")
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
            this.menuService.open(player, ComprarAccionesServerConfirmacion.class, ofertasAccionesServerService.getById(ofertaId));
        }
    }

    private void cancelarOferta(Player player, UUID ofertaId) {
        this.cancelarOfertaAccionServerUseCase.cancelar(player.getName(), ofertaId);
        this.enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Has cancelado tu oferta en el mercado. Para ver tu cartera " + AQUA + "/empresas misacciones",
                Sound.ENTITY_PLAYER_LEVELUP);

        this.reloadMenu();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class, this.jugadoresService.getByNombre(player.getName()));
    }

    private List<ItemStack> buildItemsOfertas() {
        return this.ofertasAccionesServerService.findAll().stream()
                .map(this::buildItemOferta)
                .toList();
    }

    private ItemStack buildItemOferta(OfertaAccionServer oferta) {
        boolean esOwerDeOferta = oferta.getNombreOfertante().equalsIgnoreCase(player.getName());

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
        var newMenu = this.menuService.open(player, VerOfertasAccionesServerMenu.class);
        this.syncMenuService.sync(newMenu);
    }

    @Override
    public void afterShow(Player player) {
        this.player = player;
    }
}
