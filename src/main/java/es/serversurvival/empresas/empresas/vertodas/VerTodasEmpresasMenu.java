package es.serversurvival.empresas.empresas.vertodas;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.miempresa.VerEmpresaMenu;
import es.serversurvival.empresas.empresas.solicitarservicio.SolicitarServicioUseCase;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class VerTodasEmpresasMenu extends Menu {
    private static final String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "        EMPRESAS";

    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final SolicitarServicioUseCase solicitarServicioUseCase;
    private final Player player;
    private final MenuService menuService;

    public VerTodasEmpresasMenu(Player player) {
        this.player = player;
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.solicitarServicioUseCase = new SolicitarServicioUseCase();
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0 },
                {2, 0, 0, 0, 0, 0, 0, 0, 0 },
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
                .title(TITULO)
                .item(1, buildItemInfo())
                .items(2, buildItemsEmpresas(), this::onItemEmpresaClicked)
                .breakpoint(7, Material.GREEN_BANNER, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new PerfilMenu(player.getName()));
    }

    private void onItemEmpresaClicked(Player player, InventoryClickEvent event) {
        String empresaNombre = ItemUtils.getLore(event.getCurrentItem(), 0).split(" ")[1];
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);

        if(empresa.getOwner().equalsIgnoreCase(player.getName())){
            this.menuService.open(player, new VerEmpresaMenu(empresa));
        }else{
            this.solicitarServicioUseCase.solicitar(player.getName(), empresaNombre);
            enviarMensajeYSonido(player, ChatColor.GOLD + "Has solicitado el servicio", Sound.ENTITY_PLAYER_LEVELUP);
            player.closeInventory();
        }
    }

    private List<ItemStack> buildItemsEmpresas() {
        return this.empresasService.findAll().stream()
                .map(this::buildItemEmpresa)
                .toList();
    }

    private ItemStack buildItemEmpresa(Empresa empresa) {
        boolean jugadorMenuOwnerEmpresa = empresa.getOwner().equalsIgnoreCase(this.player.getName());
        double margen = Funciones.rentabilidad(empresa.getIngresos(), empresa.getIngresos() - empresa.getGastos());
        List<String> empleadosNombres = this.empleadosService.findByEmpresa(empresa.getNombre()).stream()
                .map(Empleado::getNombre)
                .toList();

        return ItemBuilder.of(Material.getMaterial(empresa.getIcono()))
                .title(jugadorMenuOwnerEmpresa ?
                        GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER TU EMPRESA" :
                        GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA SOLICITAR UN SERVICIO")
                .lore(List.of(
                        GOLD + "Empresa: " + empresa.getNombre(),
                        GOLD + "Owner: " + GOLD + empresa.getOwner(),
                        empresa.isCotizada() ? GOLD + "Cotiza en bolsa" : GOLD + "No cotiza en bolsa",
                        "     ",
                        GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(empresa.getPixelcoins()) + " PC",
                        GOLD + "Ingresos: " + GREEN + FORMATEA.format(empresa.getIngresos()) + " PC",
                        GOLD + "Gastos: " + GREEN + FORMATEA.format(empresa.getGastos()) + " PC",
                        GOLD + "Beneficios: " + GREEN + FORMATEA.format(empresa.getIngresos() - empresa.getGastos()) + " PC",
                        GOLD + "Rentabilidad: " + GREEN + ((int) margen) + "%",
                        "      ",
                        GOLD + (empleadosNombres.isEmpty() ? "Sin trabajadores" : GOLD + "Empleados: " + empleadosNombres)
                ))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Puedes crear un empresa, contratar",
                        "empledos, sacar a bolsa, facturar etc",
                        "Para mas informacion /empresas help"
                ))
                .build();
    }
}
