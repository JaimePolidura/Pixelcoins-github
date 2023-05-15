package es.serversurvival.v1.bolsa.activosinfo.vervalores;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.activosinfo.vervalores.acciones.AccionesMenu;
import es.serversurvival.v1.bolsa.activosinfo.vervalores.criptomonedas.CriptomonedasMenu;
import es.serversurvival.v1.bolsa.activosinfo.vervalores.materiasprimas.MateriasPrimasMenu;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ExecutorService;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ElegirInversionMenu extends Menu {
    public static final String TITULO = DARK_RED + "" + BOLD + "      ELEGIR INVERSION";

    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ActivosInfoService activosInfoService;
    private final JugadoresService jugadoresService;
    private final ExecutorService executor;
    private final MenuService menuService;

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
                .item(1, itemAcciones(), (p, e) -> this.menuService.open(p, AccionesMenu.class))
                .item(2, itemCriptomonedas(), (p, e) -> this.menuService.open(p, CriptomonedasMenu.class))
                .item(3, itemMateriasPrimas(), (p, e) -> this.menuService.open(p, MateriasPrimasMenu.class))
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
