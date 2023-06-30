package es.serversurvival.minecraftserver.empresas.misempleos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.jugadores.perfil.PerfilMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.irseempleo.DejarEmpleoParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class MisEmpleosMenu extends Menu {
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    private Set<UUID> empleosIdDondeEsDirector = new HashSet<>();

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
                .breakpoint(7, MenuItems.GO_BACK, (p, e) -> menuService.open(p, PerfilMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .backward(8, Material.RED_WOOL)
                        .forward(9, Material.GREEN_WOOL)
                        .build())
                .build();
    }

    private void dejarEmpleo(Player player, InventoryClickEvent event) {
        UUID empleadoId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
        UUID empresaId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 1);

        if(empleosIdDondeEsDirector.contains(empleadoId)){
            return;
        }

        useCaseBus.handle(new DejarEmpleoParametros(player.getUniqueId(), empresaId));

        menuService.close(player);

        player.sendMessage(GOLD + "Te has ido del trabajo");
    }

    private List<ItemStack> buildItemsEmpleos(Player player) {
        return this.empleadosService.findByEmpleadoJugadorId(player.getUniqueId()).stream()
                .filter(Empleado::isEstaContratado)
                .map(this::buildItemEmpleo)
                .toList();
    }

    private ItemStack buildItemEmpleo(Empleado empleado) {
        Empresa empresaTrabajo = this.empresasService.getById(empleado.getEmpresaId());
        Material materialIcono = Material.getMaterial(empresaTrabajo.getLogotipo());
        boolean esDirector = empresaTrabajo.getDirectorJugadorId().equals(empleado.getEmpleadoJugadorId());

        if(esDirector){
            empleosIdDondeEsDirector.add(empleado.getEmpleadoId());
        }

        return ItemBuilder.of(materialIcono)
                .title(esDirector ?
                        DARK_RED + "" +BOLD + "ERES EL DIRECTOR" :
                        MenuItems.CLICKEABLE + "DEJAR EMPLEO")
                .lore(List.of(
                        GOLD + "Empresa: " + empresaTrabajo.getNombre(),
                        GOLD + "Cargo: " + empleado.getDescripccion(),
                        GOLD + "Sueldo: " + formatPixelcoins(empleado.getSueldo()) + "/ " + Funciones.millisToDias(empleado.getPeriodoPagoMs()) + " dias",
                        GOLD + "Ultima vez que te pagaron: " + Funciones.toString(empleado.getFechaUltimoPago()),
                        "   ",
                        empresaTrabajo.getEmpresaId().toString(),
                        empleado.getEmpleadoId().toString()
                ))
                .build();
    }

    //TODO
    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "INFO")
                .lore(List.of(
                        ""
                ))
                .build();
    }
}
