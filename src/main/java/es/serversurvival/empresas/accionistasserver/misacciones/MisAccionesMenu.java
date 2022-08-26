package es.serversurvival.empresas.accionistasserver.misacciones;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver.misacciones.vender.VenderAccionEmpresaCantidadSelectorMenu;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver.VerOfertasAccionesServerMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

public final class MisAccionesMenu extends Menu {
    private final AccionistasServerService accionistasServerService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final String jugadorNombre;
    private final List<OfertaAccionServer> ofertaAccionServersJugador;
    private final MenuService menuService;

    public MisAccionesMenu(String jugadorNombre) {
        this.accionistasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
        this.jugadorNombre = jugadorNombre;
        this.ofertaAccionServersJugador = ofertasAccionesServerService.findByOfertanteNombre(jugadorNombre);
        this.menuService = DependecyContainer.get(MenuService.class);
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
                .title(DARK_RED + "" + BOLD + "   TUS ACCIONES SERVER")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(2, buildItemMercado(), this::goToMercadoAccionesServerMenu)
                .items(3, buildItemAcciones(), this::venderAccion)
                .breakpoint(7, Material.RED_BANNER)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void venderAccion(Player player, InventoryClickEvent event) {
        UUID idAccion = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 5));
        List<OfertaAccionServer> ofertasAccionesInMercado = this.ofertaAccionServersJugador.stream()
                .filter(oferta -> oferta.getAccionistaEmpresaServerId().equals(idAccion))
                .toList();
        int allAccionesInMercado = ofertasAccionesInMercado.stream()
                .mapToInt(OfertaAccionServer::getCantidad)
                .sum();

        AccionistaServer accionistaServerToVender = this.accionistasServerService.getById(idAccion);
        boolean alreadyAllInMercado = !ofertasAccionesInMercado.isEmpty() &&
                allAccionesInMercado == accionistaServerToVender.getCantidad();

        if(alreadyAllInMercado){
            player.sendMessage(DARK_RED + "Ya has vendido eso en el mercado, para cancelarlo /empresas mercado y click");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        int maxQuantityThatCanBeSold = ofertasAccionesInMercado.isEmpty() ?
                accionistaServerToVender.getCantidad() :
                accionistaServerToVender.getCantidad() - allAccionesInMercado;

        this.menuService.open(player, new VenderAccionEmpresaCantidadSelectorMenu(
                maxQuantityThatCanBeSold, accionistaServerToVender
        ));
    }

    private void goToMercadoAccionesServerMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new VerOfertasAccionesServerMenu(player));
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Acciones que tienes de empresas del servidor",
                        "que cotizan en bolsa. Con estas puedes recibir",
                        "dividendos de las empresas y ser en parte dueño de ellas",
                        "Para comprar /empresas mercado Para vender las que",
                        "tengas click en ellas"
                ))
                .build();
    }

    private ItemStack buildItemMercado() {
        return ItemBuilder.of(Material.CHEST)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "VER MERCADO")
                .build();
    }

    private List<ItemStack> buildItemAcciones() {
        return this.accionistasServerService.findByNombreAccionista(jugadorNombre).stream()
                .map(this::buildItemAccionista)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemAccionista(AccionistaServer accionistaServer) {
        return ItemBuilder.of(Material.RED_BANNER)
                .title(GOLD + "" + BOLD + "CLICK PARA VENDER")
                .lore(List.of(
                        GOLD + "Empresa: " + accionistaServer.getEmpresa(),
                        GOLD + "Acciones: " + accionistaServer.getCantidad(),
                        GOLD + "Precio apertura: " + GREEN + FORMATEA.format(accionistaServer.getPrecioApertura()) + "PC",
                        GOLD + "Fecha apertura: " + accionistaServer.getFechaApertura(),
                        GOLD + "",
                        String.valueOf(accionistaServer.getAccionistaServerId())
                ))
                .build();
    }

}
