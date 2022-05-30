package es.serversurvival.bolsa.activosinfo.vervalores;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo.vervalores.acciones.AccionesMenu;
import es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas.CriptomonedasMenu;
import es.serversurvival.bolsa.activosinfo.vervalores.materiasprimas.MateriasPrimasMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ElegirInversionMenu extends Menu {
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
                .fixedItems()
                .staticMenu()
                .item(1, itemAcciones(), (p, e) -> this.menuService.open(p, new AccionesMenu()))
                .item(2, itemCriptomonedas(), (p, e) -> this.menuService.open(p, new CriptomonedasMenu()))
                .item(3, itemMateriasPrimas(), (p, e) -> this.menuService.open(p, new MateriasPrimasMenu()))
                .build();
    }

    private ItemStack itemMateriasPrimas() {
        return ItemBuilder.of(Material.CHARCOAL)
                .title(ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas")
                .build();
    }

    private ItemStack itemCriptomonedas() {
        return ItemBuilder.of(Material.GOLD_INGOT)
                .title(ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas")
                .build();
    }

    private ItemStack itemAcciones() {
        return ItemBuilder.of(Material.PAPER)
                .title(ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones")
                .build();
    }
}
