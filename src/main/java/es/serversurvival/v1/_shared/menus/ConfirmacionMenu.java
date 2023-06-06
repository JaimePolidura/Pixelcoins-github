package es.serversurvival.v1._shared.menus;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.confirmation.ConfirmationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

public abstract class ConfirmacionMenu<T> extends Menu<T> {
    @Override
    public final int[][] items() {
        return new int[][]{{1, 0, 0, 0, 2}};
    }

    @Override
    public final MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(titulo())
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(1, cancelarItem(), this::onCancelar)
                        .accept(2, aceptarItem(), (player, event) -> onAceptar(player, event, getState()))
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

    public abstract void onAceptar(Player destinatario, InventoryClickEvent event, T state);
    public abstract ItemStack aceptarItem();
}
