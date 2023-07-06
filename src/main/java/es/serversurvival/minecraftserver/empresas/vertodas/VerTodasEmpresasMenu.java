package es.serversurvival.minecraftserver.empresas.vertodas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.empresas.mercado.MercadoAccionesEmpresasMenu;
import es.serversurvival.minecraftserver.empresas.miempresa.MiEmpresaMenu;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.empresas.misacciones.MisEmpresasAccionesMenu;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerTodasEmpresasMenu extends Menu {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final MovimientosService movimientosService;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    private Set<UUID> empresasIdDondeElJugadorEsAccionista = new HashSet<>();

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 3, 0, 0, 0, 0, 0, 0 },
                {4, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(DARK_RED + "" + BOLD + "   TODAS LAS EMPRESAS")
                .item(1, buildItemInfo())
                .item(2, buildItemVerTusAcciones(), (p, e) -> menuService.open(p, MisEmpresasAccionesMenu.class))
                .item(3, buidlItemVerMercado(), (p, e) -> menuService.open(p, MercadoAccionesEmpresasMenu.class))
                .items(4, this::buildItemsEmpresas, this::onItemEmpresaClicked)
                .breakpoint(7, MenuItems.GO_MENU_BACK, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private ItemStack buidlItemVerMercado() {
        return ItemBuilder.of(Material.CHEST)
                .title(MenuItems.CLICKEABLE + "VER MERCADO DE EMPRESAS SERVER")
                .build();
    }

    private ItemStack buildItemVerTusAcciones() {
        return ItemBuilder.of(Material.CREEPER_BANNER_PATTERN)
                .title(MenuItems.CLICKEABLE + "VER TUS ACCIONES")
                .build();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private void onItemEmpresaClicked(Player player, InventoryClickEvent event) {
        Empresa empresa = this.empresasService.getById(MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0));

        if(empresasIdDondeElJugadorEsAccionista.contains(empresa.getEmpresaId())) {
            menuService.open(player, MiEmpresaMenu.class, empresa);
        }
    }

    private List<ItemStack> buildItemsEmpresas(Player player) {
        return empresasService.findAllNoCerradas().stream()
                .map(empresa -> buildItemEmpresa(empresa, player))
                .toList();
    }

    private ItemStack buildItemEmpresa(Empresa empresa, Player player) {
        boolean esAccionista = accionistasEmpresasService.findByEmpresaIdAndJugadorId(empresa.getEmpresaId(), player.getUniqueId()).isPresent();
        List<String> empleadosNombres = this.empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId()).stream()
                .map(empleado -> jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()))
                .toList();

        if(esAccionista){
            empresasIdDondeElJugadorEsAccionista.add(empresa.getEmpresaId());
        }

        return ItemBuilder.of(Material.getMaterial(empresa.getLogotipo()))
                .title(esAccionista ?
                        MenuItems.CLICKEABLE + "VER TU EMPRESA" :
                        GOLD + "" + BOLD + "" + empresa.getNombre())
                .lore(List.of(
                        GOLD + "Empresa: " + empresa.getNombre(),
                        GOLD + "Director: " + jugadoresService.getNombreById(empresa.getDirectorJugadorId()),
                        GOLD + "Fundador: " + jugadoresService.getNombreById(empresa.getFundadorJugadorId()),
                        GOLD + "Descripccion: " + empresa.getDescripcion(),
                        empresa.isEsCotizada() ? GOLD + "Cotiza en bolsa" : GOLD + "No cotiza en bolsa",
                        "     ",
                        GOLD + "Pixelcoins: " + formatPixelcoins(movimientosService.getBalance(empresa.getEmpresaId())),
                        GOLD + "Nº Total acciones: " + empresa.getNTotalAcciones(),
                        "      ",
                        GOLD + (empleadosNombres.isEmpty() ? "Sin trabajadores" : GOLD + "Empleados: " + empleadosNombres),
                        empresa.getEmpresaId().toString()
                ))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        GOLD + "Puedes crear un empresa, contratar a jugadores",
                        GOLD + "facturar, vender acciones etc",
                        GOLD + "Para mas informacion "+AQUA+"/empresas ayuda",
                        " ",
                        AQUA + "/empresas crear <nombre> <descripccion>",
                        AQUA + "/empresas pagar <nombre empresa> <pixelcoins>"
                ))
                .build();
    }


}
