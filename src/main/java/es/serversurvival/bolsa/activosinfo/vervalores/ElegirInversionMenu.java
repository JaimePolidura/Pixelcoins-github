package es.serversurvival.bolsa.activosinfo.vervalores;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo.vervalores.acciones.AccionesMenu;
import es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas.CriptomonedasMenu;
import es.serversurvival.bolsa.activosinfo.vervalores.materiasprimas.MateriasPrimasMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public final class ElegirInversionMenu extends Menu {
    public static final String TITULO = DARK_RED + "" + BOLD + "      ELEGIR INVERSION";

    private final MenuService menuService;

    public ElegirInversionMenu() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 2, 0, 3}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .staticMenu()
                .item(1, itemAcciones(), (p, e) -> this.menuService.open(p, new AccionesMenu(p.getName())))
                .item(2, itemCriptomonedas(), (p, e) -> this.menuService.open(p, new CriptomonedasMenu(p.getName())))
                .item(3, itemMateriasPrimas(), (p, e) -> this.menuService.open(p, new MateriasPrimasMenu(p.getName())))
                .build();
    }

    private ItemStack itemMateriasPrimas() {
        return ItemBuilder.of(Material.CHARCOAL)
                .title(GOLD + "" + BOLD + "Materias primas")
                .build();
    }

    private ItemStack itemCriptomonedas() {
        return ItemBuilder.of(Material.GOLD_INGOT)
                .title(GOLD + "" + BOLD + "Criptomonedas")
                .build();
    }

    private ItemStack itemAcciones() {
        return ItemBuilder.of(Material.PAPER)
                .title(GOLD + "" + BOLD + "Acciones")
                .build();
    }
}
