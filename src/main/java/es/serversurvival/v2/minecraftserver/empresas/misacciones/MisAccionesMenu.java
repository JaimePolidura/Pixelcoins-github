package es.serversurvival.v2.minecraftserver.empresas.misacciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.v2.minecraftserver.empresas.mercado.MercadoAccionesEmpresasMenu;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class MisAccionesMenu extends Menu {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
    private final OfertasService ofertasService;
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
                .item(2, buildItemMercado(), (p, e) -> menuService.open(p, MercadoAccionesEmpresasMenu.class))
                .items(3, this::buildItemAcciones, this::venderAccion)
                .breakpoint(7, Material.RED_BANNER)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void venderAccion(Player player, InventoryClickEvent event) {
        UUID accionistaId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
        AccionistaEmpresa accionistaEmpresa = accionistasEmpresasService.getById(accionistaId);
        Optional<Oferta> ofertaAccionesOptional = ofertasService.findByObjetoAndTipo(accionistaId.toString(), TipoOferta.ACCIONES_SERVER_JUGADOR);
        int cantidadMaximaQueSePuedeVender = 0;

        if(ofertaAccionesOptional.isEmpty()){
            cantidadMaximaQueSePuedeVender = accionistaEmpresa.getNAcciones();
        }
        if(ofertaAccionesOptional.isPresent()){
            cantidadMaximaQueSePuedeVender = accionistaEmpresa.getNAcciones() - ofertaAccionesOptional.get().getCantidad();
        }

        if(cantidadMaximaQueSePuedeVender == 0){
            player.sendMessage(DARK_RED + "Ya has vendido eso en el mercado, para cancelarlo /empresas mercado y click para retirar");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        Empresa empresa = empresasService.getById(accionistaEmpresa.getEmpresaId());

        this.menuService.open(player, VenderAccionEmpresaCantidadSelectorMenu.class, VenderAccionEmpresaCantidadSelectorMenu.of(
                cantidadMaximaQueSePuedeVender, accionistaId, empresa
        ));
    }

    private ItemStack buildItemInfo() {
        //TODO
        return null;
    }

    private ItemStack buildItemMercado() {
        return ItemBuilder.of(Material.CHEST)
                .title(GOLD + "" + BOLD + "" + UNDERLINE + "VER MERCADO")
                .build();
    }

    private List<ItemStack> buildItemAcciones(Player player) {
        return this.accionistasEmpresasService.findByJugadorId(player.getUniqueId()).stream()
                .map(this::buildItemAccionista)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemAccionista(AccionistaEmpresa accion) {
        return ItemBuilder.of(Material.RED_BANNER)
                .title(GOLD + "" + BOLD + "CLICK PARA VENDER")
                .lore(List.of(
                        GOLD + "Empresa: " + empresasService.getById(accion.getEmpresaId()).getNombre(),
                        GOLD + "Acciones: " + accion.getNAcciones(),
                        GOLD + "",
                        accion.getAccionistaId().toString()
                ))
                .build();
    }

}
