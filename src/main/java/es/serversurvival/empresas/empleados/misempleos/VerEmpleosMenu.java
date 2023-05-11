package es.serversurvival.empresas.empleados.misempleos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados.irse.IrseEmpresaUseCase;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VerEmpleosMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "        TUS EMPLEOS";

    private final IrseEmpresaUseCase irseEmpresaUseCase;
    private final EmpleadosService empleadosService;
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
                .title(TITULO)
                .item(1, buildItemInfo())
                .items(2, this::buildItemsEmpleos, this::dejarEmpleo)
                .breakpoint(7, Material.RED_BANNER, this::goBackToProfileMenu)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void goBackToProfileMenu(Player player, InventoryClickEvent event) {
        this.menuService.open(player, new PerfilMenu(player.getName()));
    }

    private void dejarEmpleo(Player player, InventoryClickEvent event) {
        String empresaNombre = ItemUtils.getLore(event.getCurrentItem(), 1).split(" ")[1];

        this.irseEmpresaUseCase.irse(player.getName(), empresaNombre);
        this.menuService.close(player);

        player.sendMessage(GOLD + "Te has ido del trabajo");
    }

    private List<ItemStack> buildItemsEmpleos(Player player) {
        return this.empleadosService.findByJugador(player.getName()).stream()
                .map(this::buildItemEmpleo)
                .toList();
    }

    private ItemStack buildItemEmpleo(Empleado empleado) {
        Empresa empresaTrabajo = this.empresasService.getByNombre(empleado.getEmpresa());
        Material materialIcono = Material.getMaterial(empresaTrabajo.getIcono());
        String displayName = RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA IRTE";

        return ItemBuilder.of(materialIcono)
                .title(displayName)
                .lore(List.of(
                        "   ",
                        GOLD + "Empresa: " + empleado.getEmpresa(),
                        GOLD + "Cargo: " + empleado.getCargo(),
                        GOLD + "Sueldo: " + GREEN + FORMATEA.format(empleado.getSueldo()) + "PC" + "/" + empleado.getTipoSueldo().nombre,
                        GOLD + "Ultima vez que te pagaron: " + empleado.getFechaUltimaPaga(),
                        "   ",
                        GOLD + "ID: " + empleado.getEmpleadoId()
                ))
                .build();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        "Puedes estar contratado en",
                        "una empresa y ganar dinero.",
                        "Mas info en /ayuda empleo o en:",
                        "http://serversurvival.ddns.net"

                ))
                .build();
    }
}
