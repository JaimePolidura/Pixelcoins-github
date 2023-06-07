package es.serversurvival.minecraftserver.empresas.vertodas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver.empresas.miempresa.MiEmpresaMenu;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VerTodasEmpresasMenu extends Menu<Object> {
    private final TransaccionesService transaccionesService;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final MenuService menuService;

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
                .title(DARK_RED + "" + BOLD + "        EMPRESAS")
                .item(1, buildItemInfo())
                .items(2, this::buildItemsEmpresas, this::onItemEmpresaClicked)
                .breakpoint(7, Material.GREEN_BANNER, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, PerfilMenu.class);
    }

    private void onItemEmpresaClicked(Player player, InventoryClickEvent event) {
        Empresa empresa = this.empresasService.getById(MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0));

        if(empresa.getDirectorJugadorId().equals(player.getUniqueId())) {
            this.menuService.open(player, MiEmpresaMenu.class, empresa);
        }
    }

    private List<ItemStack> buildItemsEmpresas(Player player) {
        return empresasService.findAllNoCerradas().stream()
                .map(empresa -> buildItemEmpresa(empresa, player))
                .toList();
    }

    private ItemStack buildItemEmpresa(Empresa empresa, Player player) {
        boolean jugadorMenuOwnerEmpresa = empresa.getDirectorJugadorId().equals(player.getUniqueId());
        List<String> empleadosNombres = this.empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId()).stream()
                .map(empleado -> jugadoresService.getNombreById(empleado.getEmpleadoJugadorId()))
                .toList();

        return ItemBuilder.of(Material.getMaterial(empresa.getIcono()))
                .title(jugadorMenuOwnerEmpresa ?
                        GOLD + "" + BOLD + "" + UNDERLINE + "CLICK PARA VER TU EMPRESA" :
                        GOLD + "" + BOLD + "" + empresa.getNombre())
                .lore(List.of(
                        GOLD + "Empresa: " + empresa.getNombre(),
                        GOLD + "Director: " + jugadoresService.getNombreById(empresa.getDirectorJugadorId()),
                        GOLD + "Fundador: " + jugadoresService.getNombreById(empresa.getFundadorJugadorId()),
                        GOLD + "Descripccion: " + empresa.getDescripcion(),
                        empresa.isEsCotizada() ? GOLD + "Cotiza en bolsa" : GOLD + "No cotiza en bolsa",
                        "     ",
                        GOLD + "Pixelcoins: " + GREEN + FORMATEA.format(transaccionesService.getBalancePixelcions(empresa.getEmpresaId())) + " PC",
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
                        "Puedes crear un empresa, contratar",
                        "empledos, sacar a bolsa, facturar etc",
                        "Para mas informacion /empresas help"
                ))
                .build();
    }
}
