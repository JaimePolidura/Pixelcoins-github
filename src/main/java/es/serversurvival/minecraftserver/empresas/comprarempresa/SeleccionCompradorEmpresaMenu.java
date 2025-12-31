package es.serversurvival.minecraftserver.empresas.comprarempresa;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival.minecraftserver._shared.menus.MenuItems.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public class SeleccionCompradorEmpresaMenu extends Menu<Empresa> {
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][]{{0, 1, 0, 2, 0}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(DARK_RED + "" + BOLD + "Seleccionar comprador")
                .item(1, buildItemModoCompraPersona(), (p, e) -> abrirMenuComprarPersona(p))
                .item(2, buildItemModoCompraFusionEmpresa(), (p, e) -> abrirMenuCompradorEmpresa(p))
                .build();
    }

    private void abrirMenuCompradorEmpresa(Player p) {
        menuService.open(p, SeleccionEmpresaCompradoraMenu.class, getState());
    }

    private void abrirMenuComprarPersona(Player p) {
        menuService.open(p, SeleccionPrecioComprarEmpresaMenu.class,
                SeleccionPrecioComprarEmpresaMenu.tipoCompradorJugador(getState()));
    }

    private ItemStack buildItemModoCompraPersona() {
        return ItemBuilder.of(Material.PLAYER_HEAD)
                .title(CLICKEABLE + "TU")
                .lore(GOLD + "Tu adquirias está empresa. El dinero que se utilizara sera el tuyo.")
                .build();
    }

    private ItemStack buildItemModoCompraFusionEmpresa() {
        return ItemBuilder.of(Material.WRITTEN_BOOK)
                .title(CLICKEABLE + "EMPRESA")
                .lore(List.of(
                        GOLD + "Una de las empresas de la cual seas director, adquirira esta empresa.",
                        GOLD + "La empresa" + getState().getNombre() + " se fusionara con la empresa compradora.",
                        GOLD + "El dinero que se utilizara sera de le empresa."
                ))
                .build();
    }
}
