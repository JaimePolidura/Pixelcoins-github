package es.serversurvival._shared.menus;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper.menus.Menu;
import es.bukkitclassmapper.menus.configuration.MenuConfiguration;
import es.bukkitclassmapper.menus.modules.confirmation.ConfirmationConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public abstract class ConfirmacionMenu extends Menu {
    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 0, 0, 2}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(titulo())
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(1, cancelarItem(), this::onCancelar)
                        .accept(2, aceptarItem(), this::onAceptar)
                        .build())
                .build();
    }

    private ItemStack cancelarItem() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "CANCELAR").build();
    }

    private void onCancelar(Player player, InventoryClickEvent event) {
        player.sendMessage(GOLD + "Lo has cancelado");
    }

    public String titulo(){
        return DARK_RED + "" + BOLD + "          CONFIRMAR";
    }

    public abstract void onAceptar(Player player, InventoryClickEvent event);
    public abstract ItemStack aceptarItem();
}
