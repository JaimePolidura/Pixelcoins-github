package es.serversurvival.minecraftserver.retos.verretos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

@RequiredArgsConstructor
public final class VerRetosModulosMenu extends Menu {
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 3, 4, 5}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .staticMenu()
                .fixedItems()
                .title(MenuItems.TITULO_MENU + "            RETOS")
                .item(1, buildItemRetoModulo(PLAYER_HEAD, "BASICOS"), (p, e) -> openMenuVerRetos(ModuloReto.JUGADORES))
                .item(2, buildItemRetoModulo(CHEST, "TIENDA"), (p, e) -> openMenuVerRetos(ModuloReto.TIENDA))
                .item(3, buildItemRetoModulo(DIAMOND_SWORD, "DEUDAS"), (p, e) -> openMenuVerRetos(ModuloReto.DEUDAS))
                .item(4, buildItemRetoModulo(CREEPER_BANNER_PATTERN, "EMPRESAS"), (p, e) -> openMenuVerRetos(ModuloReto.EMPRESAS))
                .item(5, buildItemRetoModulo(NAME_TAG, "BOLSA"), (p, e) -> openMenuVerRetos(ModuloReto.BOLSA))
                .build();
    }

    private void openMenuVerRetos(ModuloReto moduloReto) {
        menuService.open(getPlayer(), VerRetosMenu.class, VerRetosMenu.stateOf(moduloReto));
    }

    private ItemStack buildItemRetoModulo(Material material, String titulo) {
        return ItemBuilder.of(material)
                .title(MenuItems.CLICKEABLE + "RETOS " + titulo)
                .build();
    }
}
