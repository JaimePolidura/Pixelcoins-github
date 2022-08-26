package es.serversurvival.empresas.empresas.borrar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.confirmation.ConfirmationConfiguration;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public final class BorrarEmpresaConfirmacionMenu extends Menu {
    public static final String TITULO = DARK_RED + "" + BOLD + "     Confirmar borrar";

    private final String jugadorNombre;
    private final Empresa empresa;

    public BorrarEmpresaConfirmacionMenu(String jugadorNombre, Empresa empresa) {
        this.jugadorNombre = jugadorNombre;
        this.empresa = empresa;
    }

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 0, 0, 2}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(1, buildItemCancel(), this::onCancel)
                        .accept(2, buildItemAccept(), this::onAccept)
                        .build())
                .build();
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        BorrarEmpresaUseCase.INSTANCE.borrar(player.getName(), empresa.getNombre());

        player.sendMessage(ChatColor.GOLD + "Has borrado tu empresa, has recibido todas las pixelcoins de ello");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void onCancel(Player player, InventoryClickEvent event) {
        player.sendMessage(GOLD + "Has cancelado");
    }

    private ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "BORRAR").build();
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "CANCELAR").build();
    }
}
