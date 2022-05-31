package es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.cancelarofertaccionserver.CancelarOfertaAccionServerUseCase;
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
    private final Player player;

    public VerOfertasAccionesServerMenu(Player player) {
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
        this.player = player;
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
                .items(2, buildItemsOfertas(), this::onOfertaClcked)
                .breakpoint(7, RED_BANNER, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, RED_WOOL)
                        .forward(9, GREEN_WOOL)
                        .build())
                .build();
    }

    private void onOfertaClcked(Player player, InventoryClickEvent event) {
        boolean esOwnerDeOferta = event.getCurrentItem().getType() == BLUE_BANNER;
        UUID ofertaId = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 9));

        if(esOwnerDeOferta){
            cancelarOferta(player, ofertaId);
        }else{
            //TODO Enviar comprarofertaconfirmatcion
        }
    }

    private void cancelarOferta(Player player, UUID ofertaId) {
        (new CancelarOfertaAccionServerUseCase()).cancelar(player.getName(), ofertaId);
        enviarMensajeYSonido(player, GOLD + "Has cancelado tu oferta en el mercado. Ahora vuelves a tener esas acciones en tu cartera: " + AQUA + "/bolsa cartera",
                Sound.ENTITY_PLAYER_LEVELUP);
        player.closeInventory();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        //TODO
    }

    private List<ItemStack> buildItemsOfertas() {
        return this.ofertasAccionesServerService.findAll().stream()
                .map(this::buildItemOferta)
                .toList();
    }

    private ItemStack buildItemOferta(OfertaAccionServer oferta) {
        boolean esOwerDeOferta = oferta.getNombreOfertante().equalsIgnoreCase(this.player.getName());
        boolean tipoJugador = oferta.esTipoOfertanteJugador();

        return ItemBuilder.of(esOwerDeOferta ? RED_BANNER : tipoJugador ? GREEN_BANNER : BLUE_BANNER)
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
                        "" + oferta.getNombreOfertante()
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
                        "vender las acciones de las empresas"
                ))
                .build();
    }
}
