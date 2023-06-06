package es.serversurvival.v2.minecraftserver.empresas.misempleos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.v2.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.irseempleo.DejarEmpleoParametros;
import es.serversurvival.v2.pixelcoins.empresas.irseempleo.DejarEmpleoUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class MisEmpleosMenu extends Menu {
    private final DejarEmpleoUseCase dearEmpleoUseCase;
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
                .title(DARK_RED + "" + BOLD + "        TUS EMPLEOS")
                .item(1, buildItemInfo())
                .items(2, this::buildItemsEmpleos, this::dejarEmpleo)
                .breakpoint(7, Material.RED_BANNER, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void dejarEmpleo(Player player, InventoryClickEvent event) {
        UUID empresaId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);

        dearEmpleoUseCase.dejar(new DejarEmpleoParametros(player.getUniqueId(), empresaId));

        this.menuService.close(player);

        player.sendMessage(GOLD + "Te has ido del trabajo");
    }

    private List<ItemStack> buildItemsEmpleos(Player player) {
        return this.empleadosService.findByEmpleadoJugadorId(player.getUniqueId()).stream()
                .map(this::buildItemEmpleo)
                .toList();
    }

    private ItemStack buildItemEmpleo(Empleado empleado) {
        Empresa empresaTrabajo = this.empresasService.getById(empleado.getEmpresaId());
        Material materialIcono = Material.getMaterial(empresaTrabajo.getIcono());
        String displayName = RED + "" + BOLD + "" + UNDERLINE + "CLICK PARA IRTE";

        return ItemBuilder.of(materialIcono)
                .title(displayName)
                .lore(List.of(
                        "   ",
                        GOLD + "Empresa: " + empresaTrabajo.getNombre(),
                        GOLD + "Cargo: " + empleado.getDescripccion(),
                        GOLD + "Sueldo: " + GREEN + FORMATEA.format(empleado.getSueldo()) + "PC" + " / " + Funciones.millisToDias(empleado.getPeriodoPagoMs()) + " dias",
                        GOLD + "Ultima vez que te pagaron: " + empleado.getFechaUltimoPago().toString(),
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
