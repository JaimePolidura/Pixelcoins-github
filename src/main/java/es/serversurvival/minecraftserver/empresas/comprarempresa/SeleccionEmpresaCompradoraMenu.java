package es.serversurvival.minecraftserver.empresas.comprarempresa;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.bukkitbettermenus.utils.ItemUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.DARK_RED;

@AllArgsConstructor
public class SeleccionEmpresaCompradoraMenu extends Menu<Empresa> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(DARK_RED + "" + BOLD + "Selecciona empresa compradora")
                .items(1, buildItemEmpresasJugadorDirector(), this::abrirMenuSeleccionPrecio)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, SeleccionCompradorEmpresaMenu.class, getState()))
                .paginated(PaginationConfiguration.builder()
                        .forward(9, Material.GREEN_WOOL)
                        .backward(8, Material.RED_WOOL)
                        .build())
                .build();
    }

    private void abrirMenuSeleccionPrecio(Player player, InventoryClickEvent inventoryClickEvent) {
        UUID empresaCompradoraId = UUID.fromString(ItemUtils.getLore(inventoryClickEvent.getCurrentItem(), 0));
        Empresa empresaCompradora = empresasService.getById(empresaCompradoraId);
        menuService.open(player, SeleccionPrecioComprarEmpresaMenu.class,
                SeleccionPrecioComprarEmpresaMenu.tipoCompradorEmpresa(getState(), empresaCompradora));
    }

    private List<ItemStack> buildItemEmpresasJugadorDirector() {
        return empresasService.findByDirectorJugadorIdNoCerrada(getPlayer().getUniqueId()).stream()
                .map(empresa -> ItemBuilder.of(Material.getMaterial(empresa.getLogotipo()))
                        .title(MenuItems.CLICKEABLE + empresa.getNombre())
                        .lore(empresa.getEmpresaId().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
