package es.serversurvival._shared.menus;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.confirmation.ConfirmationConfiguration;
import es.jaimetruman.menus.modules.numberselector.NumberSelectorMenuConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.DECREASE;
import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.INCREASE;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public abstract class NumberSelectorMenu extends Menu {
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
                        .accept(5, buildItemAccept(), this::onAccept)
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

    public abstract double maxValue();
    public abstract double initialValue();
    public abstract List<String> loreItemAceptar(double cantidad);
    public abstract void onAccept(Player player, InventoryClickEvent event);

    public final void onCantidadChanged(double cantidad){
        super.setItemLoreActualPage(14, loreItemAceptar(cantidad));
    }

    public ItemStack buildItemAccept (){
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
