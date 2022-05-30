package es.serversurvival.bolsa.activosinfo.vervalores;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.confirmation.ConfirmationConfiguration;
import es.jaimetruman.menus.modules.numberselector.NumberSelectorMenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.*;
import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.POOL;
import static es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public final class ComprarBolsaConfirmacionMenu extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "   COMPRAR ";

    private final ComprarLargoUseCase comprarLargoUseCase;
    private final JugadoresService jugadoresService;

    private final String jugadrNombre;
    private final String nombreActivo;
    private final double precioUnidad;
    private final SupportedTipoActivo tipoActivo;
    private final double dineroJugador;

    public ComprarBolsaConfirmacionMenu(String nombreActivo, SupportedTipoActivo tipoActivo,
                                        String jugadorNombre, double precioUnidad) {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.comprarLargoUseCase = new ComprarLargoUseCase();

        this.tipoActivo = tipoActivo;
        this.nombreActivo = nombreActivo;
        this.precioUnidad = precioUnidad;
        this.jugadrNombre = jugadorNombre;
        this.dineroJugador = jugadoresService.getByNombre(jugadorNombre).getPixelcoins();
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0  },
                {1, 2, 3, 4, 0, 5, 6, 7, 8  },
                {0, 0, 0, 0, 0, 0, 0, 0, 0  }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(TITULO)
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(5, buildItemCancel(), this::onCancel)
                        .accept(5, buildItemAccept(), this::onAccept)
                        .build())
                .numberSelector(NumberSelectorMenuConfiguration.builder()
                        .initialValue(1)
                        .minValue(1)
                        .maxValue(dineroJugador / precioUnidad)
                        .valuePropertyName("cantidad")
                        .onValueChanged(this::onCantidadChangedChangeItemAcetpar)
                        .item(1, DECREASE, 1, buildItemNumberSelector(-1))
                        .item(2, DECREASE, 5, buildItemNumberSelector(-5))
                        .item(3, DECREASE, 10, buildItemNumberSelector(-10))
                        .item(6, INCREASE, 1, buildItemNumberSelector(1))
                        .item(7, INCREASE, 5, buildItemNumberSelector(5))
                        .item(8, INCREASE, 10, buildItemNumberSelector(10))
                        .build())
                .build();
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        POOL.submit(() -> {
            int cantidadAComprar = (int) super.getPropertyDouble("cantidad");
            double pixelcoinsJugador = this.jugadoresService.getByNombre(jugadrNombre).getPixelcoins();

            if (pixelcoinsJugador < (cantidadAComprar * precioUnidad)) {
                player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                return;
            }

            OrderExecutorProxy.execute(AbrirOrdenPremarketCommand.of(player.getName(), nombreActivo, cantidadAComprar, LARGO_COMPRA, null),
                    () -> {
                        comprarLargoUseCase.comprarLargo(player.getName(), tipoActivo, nombreActivo, cantidadAComprar);
                    }
            );
        });
    }

    private void onCantidadChangedChangeItemAcetpar(double nuevaCantidad) {
        super.setItemLore(14, 0, GOLD + "Comprar " + nuevaCantidad);
        super.setItemLore(14, 2, GOLD + "Precio/Total " + GREEN + FORMATEA.format(precioUnidad * nuevaCantidad) + " PC");
    }

    private ItemStack buildItemNumberSelector(int i) {
        Material material = i < 0 ? Material.RED_BANNER : Material.GREEN_BANNER;
        String title = i < 0 ? RED + "" + BOLD + i : GREEN + "" + BOLD + "+" + i;

        return ItemBuilder.of(material).title(title).build();
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.GREEN_BANNER)
                .title(RED + "" + BOLD + "CANCELAR")
                .build();
    }

    private void onCancel(Player player, InventoryClickEvent event) {
        player.sendMessage(GOLD + "Has cancelado la compra");
    }

    private ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "ACEPTAR")
                .lore(List.of(
                        GOLD + "Comprar 1",
                        GOLD + "Precio/Unidad" + GREEN + FORMATEA.format(precioUnidad) + " PC",
                        GOLD + "Precio/Total " + GREEN + FORMATEA.format(precioUnidad * 1) + " PC",
                        GOLD + "Tus pixelcoins" + GREEN + FORMATEA.format(dineroJugador) + " PC"
                ))
                .build();
    }
}
