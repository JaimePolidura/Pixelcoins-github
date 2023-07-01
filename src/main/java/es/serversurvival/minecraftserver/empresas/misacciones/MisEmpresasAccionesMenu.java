package es.serversurvival.minecraftserver.empresas.misacciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.mercado.MercadoAccionesEmpresasMenu;
import es.serversurvival.minecraftserver.empresas.vertodas.VerTodasEmpresasMenu;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
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
public final class MisEmpresasAccionesMenu extends Menu {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
    private final OfertasService ofertasService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 3, 0, 0, 0, 0, 0, 0},
                {6, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "TUS ACCIONES DE EMPRESAS")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(2, buildItemTodasLasEmpresas(), (p, e) -> menuService.open(p, VerTodasEmpresasMenu.class))
                .item(3, buildItemMercado(), (p, e) -> menuService.open(p, MercadoAccionesEmpresasMenu.class))
                .items(6, this::buildItemAcciones, this::venderAccion)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private ItemStack buildItemTodasLasEmpresas() {
        return ItemBuilder.of(Material.NETHERITE_SCRAP)
                .title(MenuItems.CLICKEABLE + "VER TODAS LAS EMPRESAS").build();
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
        return ItemBuilder.of(Material.PAPER)
                .lore(List.of(
                    GOLD + "Puedes comprar y vender acciones de empresas del servidor",
                    GOLD + "Esto te converte en parte propietario de la empresa. Por lo",
                    GOLD + "Cual puedes recibir dividendes, decidir quien gestionala la",
                    GOLD + "empresa etc..",
                    GOLD + "",
                    GOLD + "Para comprar/vender acciones:",
                    AQUA + "/empresas misacciones",
                    AQUA + "/empresas mercado",
                    GOLD + "",
                    GOLD + "Si quieres vender acciones de tu empresa: ",
                    AQUA + "/empresas ipo <nombre empresa> <nº acciones a vender>",
                    AQUA + "   <precio/accion>",
                    GOLD + "",
                    GOLD + "Para mas comandos: ",
                    AQUA + "/empresas ayuda"
                ))
                .build();
    }

    private ItemStack buildItemMercado() {
        return ItemBuilder.of(Material.CHEST)
                .title(MenuItems.CLICKEABLE + "VER MERCADO DE EMPRESAS SERVER")
                .build();
    }

    private List<ItemStack> buildItemAcciones(Player player) {
        return this.accionistasEmpresasService.findByJugadorId(player.getUniqueId()).stream()
                .map(this::buildItemAccionista)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemAccionista(AccionistaEmpresa accion) {
        return ItemBuilder.of(Material.CREEPER_BANNER_PATTERN)
                .title(MenuItems.CLICKEABLE + "VENDER")
                .lore(List.of(
                        GOLD + "Empresa: " + empresasService.getById(accion.getEmpresaId()).getNombre(),
                        GOLD + "Acciones: " + accion.getNAcciones(),
                        GOLD + "",
                        accion.getAccionistaId().toString()
                ))
                .build();
    }

}
