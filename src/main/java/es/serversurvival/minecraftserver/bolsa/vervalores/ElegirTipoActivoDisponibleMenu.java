package es.serversurvival.minecraftserver.bolsa.vervalores;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ElegirTipoActivoDisponibleMenu extends Menu {
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 0, 0, 2}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "      ELEGIR INVERSION")
                .fixedItems()
                .staticMenu()
                .item(1, itemAcciones(), (p, e) -> abrirMenuVerValoresDisponibles(p, TipoActivoBolsa.ACCION))
                .item(2, itemCriptomonedas(), (p, e) -> abrirMenuVerValoresDisponibles(p, TipoActivoBolsa.CRIPTOMONEDAS))
                .build();
    }

    private void abrirMenuVerValoresDisponibles(Player player, TipoActivoBolsa tipoActivo) {
        menuService.open(player, VerActivosDisponiblesMenu.class, tipoActivo);
    }

    private ItemStack itemCriptomonedas() {
        return ItemBuilder.of(TipoActivoBolsa.CRIPTOMONEDAS.getMaterial()).title(GOLD + "" + BOLD + "Criptomonedas").build();
    }

    private ItemStack itemAcciones() {
        return ItemBuilder.of(TipoActivoBolsa.ACCION.getMaterial()).title(GOLD + "" + BOLD + "Acciones").build();
    }
}
