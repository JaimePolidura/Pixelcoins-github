package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasaccionesserver;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.modules.confirmation.ConfirmationConfiguration;
import es.jaimetruman.menus.modules.numberselector.NumberSelectorMenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.DECREASE;
import static es.jaimetruman.menus.modules.numberselector.NumberSelectActionType.INCREASE;
import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GREEN;

public final class ComprarAccionesServerConfirmacion extends Menu {
    private static final String TITULO = DARK_RED + "" + BOLD + "   COMPRAR ";

    private final OfertaAccionServer oferta;
    private final Jugador compradorJugador;
    private final JugadoresService jugadoresService;

    public ComprarAccionesServerConfirmacion(OfertaAccionServer oferta, Player player) {
        this.oferta = oferta;
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.compradorJugador = this.jugadoresService.getByNombre(player.getName());

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
                .title(TITULO)
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(5, buildItemCancel(), this::onCancel)
                        .accept(5, buildItemAccept(), this::onAccept)
                        .build())
                .numberSelector(NumberSelectorMenuConfiguration.builder()
                        .initialValue(1)
                        .minValue(1)
                        .maxValue(calculateMaxCantidadToComprar())
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

    private double calculateMaxCantidadToComprar() {
        return this.compradorJugador.getPixelcoins() >= (oferta.getCantidad() * oferta.getPrecio()) ?
                oferta.getCantidad() :
                compradorJugador.getPixelcoins() / oferta.getPrecio();
    }

    private ItemStack buildItemNumberSelector(int i) {
        Material material = i < 0 ? Material.RED_BANNER : Material.GREEN_BANNER;
        String title = i < 0 ? RED + "" + BOLD + i : GREEN + "" + BOLD + "+" + i;

        return ItemBuilder.of(material).title(title).build();
    }

    private void onCantidadChangedChangeItemAcetpar(Double aDouble) {
        super.setItemLore(14, getLoreItemAceptar());
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        int cantidadToComprar = (int) super.getPropertyDouble("cantidad");

        new ComprarOfertaMercadoUseCase().comprarOfertaMercadoAccionServer(player.getName(),
                oferta.getOfertaAccioneServerId(), cantidadToComprar);

        enviarMensajeYSonido(player, GOLD + "Has comprado " + FORMATEA.format(cantidadToComprar) + " acciones a " + GREEN +
                FORMATEA.format(oferta.getPrecio()) + " PC" + GOLD + " que es un total de " + GREEN + FORMATEA.format(
                        cantidadToComprar * oferta.getPrecio()) + " PC " + GOLD + " comandos: " + AQUA +
                "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadToComprar + " acciones de la empresa del server: "
                + oferta.getEmpresa() + " a " + GREEN + FORMATEA.format(oferta.getPrecio()) + "PC");
    }

    private ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_BANNER)
                .title(GREEN + "" + BOLD + "COMPRAR")
                .lore(getLoreItemAceptar())
                .build();
    }

    private List<String> getLoreItemAceptar(){
        double cantidadToComprar = super.getPropertyDouble("cantidad");

        return List.of(
                GOLD + "Comprar: " + cantidadToComprar + " acciones de " + this.oferta.getEmpresa(),
                GOLD + "Precio/Accion: " + GREEN + FORMATEA.format(oferta.getPrecio()) + " PC",
                GOLD + "Total: " + GREEN + FORMATEA.format(oferta.getPrecio() * cantidadToComprar) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + FORMATEA.format(this.compradorJugador.getPixelcoins()) + " PC"
        );
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "CANCELAR").build();
    }

    private void onCancel(Player player, InventoryClickEvent event) {
        player.sendMessage(GOLD + "Has cancelado la compra");
    }
}
