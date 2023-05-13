package es.serversurvival.empresas.accionistasserver.misacciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver.misacciones.vender.VenderAccionEmpresaCantidadSelectorMenu;
import es.serversurvival.empresas.accionistasserver.misacciones.vender.VenderAccionesEmpresaCantidadSelectorMenuState;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver.VerOfertasAccionesServerMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class MisAccionesMenu extends Menu {
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final AccionistasServerService accionistasServerService;
    private final MenuService menuService;

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
                .items(3, this::buildItemAcciones, this::venderAccion)
                .breakpoint(7, Material.RED_BANNER)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void venderAccion(Player player, InventoryClickEvent event) {
        UUID idAccion = UUID.fromString(ItemUtils.getLore(event.getCurrentItem(), 5));
        List<OfertaAccionServer> ofertasAccionesInMercado = this.ofertasAccionesServerService.findByOfertanteNombre(player.getName())
                .stream()
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

        this.menuService.open(player, VenderAccionEmpresaCantidadSelectorMenu.class, VenderAccionesEmpresaCantidadSelectorMenuState.of(
                maxQuantityThatCanBeSold, idAccion, accionistaServerToVender.getEmpresa()
        ));
    }

    private void goToMercadoAccionesServerMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, VerOfertasAccionesServerMenu.class);
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

    private List<ItemStack> buildItemAcciones(Player player) {
        return this.accionistasServerService.findByNombreAccionista(player.getName()).stream()
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
