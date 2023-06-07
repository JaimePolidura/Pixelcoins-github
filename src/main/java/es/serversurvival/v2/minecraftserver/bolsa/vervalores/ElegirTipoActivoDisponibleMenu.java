package es.serversurvival.v2.minecraftserver.bolsa.vervalores;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class ElegirTipoActivoDisponibleMenu extends Menu {
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 2, 0, 3}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "      ELEGIR INVERSION")
                .fixedItems()
                .staticMenu()
                .item(1, itemAcciones(), (p, e) -> abrirMenuVerValoresDisponibles(p, TipoActivoBolsa.ACCION))
                .item(2, itemCriptomonedas(), (p, e) -> abrirMenuVerValoresDisponibles(p, TipoActivoBolsa.CRIPTOMONEDAS))
                .item(3, itemMateriasPrimas(), (p, e) -> abrirMenuVerValoresDisponibles(p, TipoActivoBolsa.MATERIA_PRIMA))
                .build();
    }

    private void abrirMenuVerValoresDisponibles(Player player, TipoActivoBolsa tipoActivo) {
        menuService.open(player, VerActivosDisponiblesMenu.class, tipoActivo);
    }

    private ItemStack itemMateriasPrimas() {
        return ItemBuilder.of(TipoActivoBolsa.MATERIA_PRIMA.getMaterial()).title(GOLD + "" + BOLD + "Materias primas").build();
    }

    private ItemStack itemCriptomonedas() {
        return ItemBuilder.of(TipoActivoBolsa.CRIPTOMONEDAS.getMaterial()).title(GOLD + "" + BOLD + "Criptomonedas").build();
    }

    private ItemStack itemAcciones() {
        return ItemBuilder.of(TipoActivoBolsa.ACCION.getMaterial()).title(GOLD + "" + BOLD + "Acciones").build();
    }
}
