package es.serversurvival.minecraftserver.empresas.miempresa;


import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.BeforeShow;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.despedir.DespedirEmpleadoParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class OpccionesEmpleadoMenu extends Menu<Empleado> implements BeforeShow {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    private String nombreEmpleado;
    private String nombreEmpresa;

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 0, 0, 5}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "          " + jugadoresService.getNombreById(getState().getEmpleadoJugadorId()))
                .fixedItems()
                .item(5, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, MiEmpresaMenu.class, empresasService.getById(getState().getEmpresaId())))
                .item(1, this::buildItemEditar, this::editarEmpleado)
                .item(2, this::buildItemDespedir, this::despedirEmpleado)
                .build();
    }

    private ItemStack buildItemDespedir(Player player) {
        return ItemBuilder.of(Material.BARRIER)
                .title(GREEN + "" + BOLD + UNDERLINE + "DESPEDIR")
                .build();
    }

    private void despedirEmpleado(Player player, InventoryClickEvent event) {
        useCaseBus.handle(DespedirEmpleadoParametros.builder()
                .empleadoIdADespedir(getState().getEmpleadoId())
                .causa("Por ser un vago")
                .empresaId(getState().getEmpresaId())
                .jugadorId(player.getUniqueId())
                .build());

        getPlayer().sendMessage(GOLD + "Has despedido a " + nombreEmpleado + " de la empresa " + nombreEmpresa);

        player.closeInventory();
    }

    private ItemStack buildItemEditar(Player player) {
        return ItemBuilder.of(Material.WRITABLE_BOOK)
                .title(GREEN + "" + BOLD + UNDERLINE + "EDTIAR EMPLEADO")
                .build();
    }

    private void editarEmpleado(Player player, InventoryClickEvent event) {
        player.closeInventory();
        player.sendMessage(GOLD + "Si queres editar su sueldo: " + AQUA  + "/empresas editarempleado " + nombreEmpresa + " " + nombreEmpleado + " sueldo <nuevoSueldo>");
        player.sendMessage(GOLD + "Si queres editar su cargo: " + AQUA  + "/empresas editarempleado " + nombreEmpresa + " " + nombreEmpleado + " cargo <nueva descripccion cargo>");
        player.sendMessage(GOLD + "Si queres editar su periodo de pago: " + AQUA  + "/empresas editarempleado " + nombreEmpresa + " " + nombreEmpleado + " periodoPago <nueva periodo pago expresado en dias>");
    }

    @Override
    public void beforeShow(Player player) {
        this.nombreEmpleado = jugadoresService.getNombreById(getState().getEmpleadoJugadorId());
        this.nombreEmpresa = empresasService.getById(getState().getEmpresaId())
                .getNombre();
    }
}
