package es.serversurvival.empresas.empleados.misempleos;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados.irse.IrseEmpresaUseCase;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

public final class VerEmpleosMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "        TUS EMPLEOS";

    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final IrseEmpresaUseCase irseEmpresaUseCase;
    private final Player player;
    private final MenuService menuService;

    public VerEmpleosMenu(Player player) {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.player = player;
        this.menuService = DependecyContainer.get(MenuService.class);
        this.irseEmpresaUseCase = new IrseEmpresaUseCase();
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
                .items(2, buildItemsEmpleos(), this::dejarEmpleo)
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

    private List<ItemStack> buildItemsEmpleos() {
        return this.empleadosService.findByJugador(this.player.getName()).stream()
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
