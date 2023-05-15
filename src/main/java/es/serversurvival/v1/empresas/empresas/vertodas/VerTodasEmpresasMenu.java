package es.serversurvival.v1.empresas.empresas.vertodas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas.miempresa.VerEmpresaMenu;
import es.serversurvival.v1.empresas.empresas.solicitarservicio.SolicitarServicioUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores.perfil.PerfilMenu;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VerTodasEmpresasMenu extends Menu<Object> {
    private final SolicitarServicioUseCase solicitarServicioUseCase;
    private final EnviadorMensajes enviadorMensajes;
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
                .title(ChatColor.DARK_RED + "" + ChatColor.BOLD + "        EMPRESAS")
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
        this.menuService.open(player, PerfilMenu.class, this.jugadoresService.getByNombre(player.getName()));
    }

    private void onItemEmpresaClicked(Player player, InventoryClickEvent event) {
        String empresaNombre = ItemUtils.getLore(event.getCurrentItem(), 0).split(" ")[1];
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);

        if(empresa.getOwner().equalsIgnoreCase(player.getName())){
            this.menuService.open(player, VerEmpresaMenu.class, empresa);
        }else{
            this.solicitarServicioUseCase.solicitar(player.getName(), empresaNombre);
            enviadorMensajes.enviarMensajeYSonido(player, ChatColor.GOLD + "Has solicitado el servicio", Sound.ENTITY_PLAYER_LEVELUP);
            player.closeInventory();
        }
    }

    private List<ItemStack> buildItemsEmpresas(Player player) {
        return this.empresasService.findAll().stream()
                .map(empresa -> buildItemEmpresa(empresa, player))
                .toList();
    }

    private ItemStack buildItemEmpresa(Empresa empresa, Player player) {
        boolean jugadorMenuOwnerEmpresa = empresa.getOwner().equalsIgnoreCase(player.getName());
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
