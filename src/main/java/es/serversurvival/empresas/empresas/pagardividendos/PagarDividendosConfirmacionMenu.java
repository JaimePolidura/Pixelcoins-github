package es.serversurvival.empresas.empresas.pagardividendos;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.confirmation.ConfirmationConfiguration;
import es.jaimetruman.menus.modules.numberselector.NumberSelectorMenuConfiguration;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.DECREASE;
import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.INCREASE;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public final class PagarDividendosConfirmacionMenu extends Menu {
    private final Empresa empresa;

    public PagarDividendosConfirmacionMenu(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 0, 5, 6, 7, 8},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "   REPARTIR DIVIDENDOS ")
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(5, buildItemCancel(), this::onCancel)
                        .accept(5, buildItemAccept(), this::onAccept)
                        .build())
                .numberSelector(NumberSelectorMenuConfiguration.builder()
                        .initialValue(1)
                        .minValue(1)
                        .maxValue(this.empresa.getPixelcoins() / this.empresa.getAccionesTotales())
                        .valuePropertyName("cantidad")
                        .onValueChanged(this::onCantidadChangedChangeItemAcetpar)
                        .item(1, DECREASE, 0.5, buildItemNumberSelector(-0.5))
                        .item(2, DECREASE, 2, buildItemNumberSelector(-2))
                        .item(3, DECREASE, 5, buildItemNumberSelector(-5))
                        .item(6, INCREASE, 0.5, buildItemNumberSelector(0.5))
                        .item(7, INCREASE, 2, buildItemNumberSelector(2))
                        .item(8, INCREASE, 5, buildItemNumberSelector(5))
                        .build())
                .build();
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        double dividendoPorAccion = super.getPropertyDouble("cantidad");
        (new PagarDividendosEmpresaServerUseCase()).pagar(player.getName(), this.empresa.getNombre(), dividendoPorAccion);
        Funciones.enviarMensajeYSonido(player, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
    }

    private ItemStack buildItemNumberSelector(double i) {
        String title = i < 0 ? RED + "" + BOLD + i : GREEN + "" + BOLD + "+" + i;

        return ItemBuilder.of(i < 0 ? Material.RED_BANNER : Material.GREEN_BANNER).title(title).build();
    }

    private ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "REPARTIR").lore(getLoreItemAceptar()).build();
    }

    private List<String> getLoreItemAceptar(){
        double dividendoPorAccion = super.getPropertyDouble("cantidad");

        return List.of(
                GOLD + "Precio/Accion: " + GREEN + Funciones.FORMATEA.format(dividendoPorAccion),
                GOLD + "Total a pagar: " + GREEN + Funciones.FORMATEA.format(dividendoPorAccion*this.empresa.getAccionesTotales()),
                GOLD + "Pixelcoins empresa: " + GREEN + Funciones.FORMATEA.format(this.empresa.getPixelcoins())
        );
    }

    private void onCantidadChangedChangeItemAcetpar(Double aDouble) {
        super.setItemLore(14, this.getLoreItemAceptar());
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "CANCELAR").build();
    }

    private void onCancel(Player player, InventoryClickEvent event) {}
}
