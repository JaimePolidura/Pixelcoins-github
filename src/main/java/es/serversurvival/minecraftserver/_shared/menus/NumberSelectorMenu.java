package es.serversurvival.minecraftserver._shared.menus;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.confirmation.ConfirmationConfiguration;
import es.bukkitbettermenus.modules.numberselector.NumberSelectorMenuConfiguration;
import es.bukkitbettermenus.modules.timers.MenuTimer;
import es.bukkitbettermenus.modules.timers.TimerExecutionType;
import es.bukkitbettermenus.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.bukkitbettermenus.modules.numberselector.NumberSelectActionType.DECREASE;
import static es.bukkitbettermenus.modules.numberselector.NumberSelectActionType.INCREASE;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public abstract class NumberSelectorMenu<T> extends Menu<T> {
    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {1, 2, 3, 4, 0, 5, 6, 7, 8 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 }
        };
    }

    protected String titulo(){
        return DARK_RED + "" + BOLD  + "          CONFIRMAR";
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(titulo())
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(4, buildItemCancel(), this::onCancel)
                        .accept(5, buildItemAccept(), (p, e) -> this.onAccept(p, getPropertyDouble("cantidad"), e))
                        .closeOnAction(closeOnAction())
                        .build())
                .numberSelector(NumberSelectorMenuConfiguration.builder()
                        .initialValue(initialValue())
                        .minValue(minValue())
                        .maxValue(maxValue())
                        .valuePropertyName("cantidad")
                        .onValueChanged(this::onCantidadChanged)
                        .item(1, DECREASE, 10, buildItemNumberSelector(-10))
                        .item(2, DECREASE, 5, buildItemNumberSelector(-5))
                        .item(3, DECREASE, 1, buildItemNumberSelector(-1))
                        .item(6, INCREASE, 1, buildItemNumberSelector(1))
                        .item(7, INCREASE, 5, buildItemNumberSelector(5))
                        .item(8, INCREASE, 10, buildItemNumberSelector(10))
                        .build())
                .build();
    }

    public abstract double maxValue();
    public abstract double initialValue();
    public abstract List<String> loreItemAceptar(double cantidad);
    public abstract void onAccept(Player player, double cantidad, InventoryClickEvent event);

    public boolean closeOnAction(){
        return true;
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL)
                .title(RED + "" + BOLD + "CANCELAR")
                .build();
    }

    public int minValue() {
        return 1;
    }

    public final void onCantidadChanged(double cantidad){
        super.setActualItemLore(14, loreItemAceptar(cantidad));
    }

    public ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(itemAcceptTitle())
                .lore(loreItemAceptar(initialValue()))
                .build();
    }

    public String itemAcceptTitle(){
        return GREEN + "" + BOLD + "ACEPTAR";
    }

    public void onCancel(Player player, InventoryClickEvent event) {
        player.sendMessage(DARK_RED + "Has cancelado");
    }

    private ItemStack buildItemNumberSelector(int i) {
        Material material = i < 0 ? Material.RED_BANNER : Material.GREEN_BANNER;
        String title = i < 0 ? RED + "" + BOLD + i : GREEN + "" + BOLD + "+" + i;

        return ItemBuilder.of(material).title(title).build();
    }
}
