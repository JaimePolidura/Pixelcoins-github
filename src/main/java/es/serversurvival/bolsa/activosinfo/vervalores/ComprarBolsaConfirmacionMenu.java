package es.serversurvival.bolsa.activosinfo.vervalores;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.NumberSelectorMenu;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.Funciones.POOL;
import static es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion.LARGO_COMPRA;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

public final class ComprarBolsaConfirmacionMenu extends NumberSelectorMenu {
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final JugadoresService jugadoresService;

    private final String jugadrNombre;
    private final String nombreActivo;
    private final double precioUnidad;
    private final TipoActivo tipoActivo;
    private final double dineroJugador;

    public ComprarBolsaConfirmacionMenu(String nombreActivo, TipoActivo tipoActivo,
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
    public void onAccept(Player player, InventoryClickEvent event) {
        POOL.submit(() -> {
            int cantidadAComprar = (int) super.getPropertyDouble("cantidad");
            double pixelcoinsJugador = this.jugadoresService.getByNombre(jugadrNombre).getPixelcoins();

            if (pixelcoinsJugador < (cantidadAComprar * precioUnidad)) {
                player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                return;
            }

            var executedInMarket = OrderExecutorProxy.execute(
                    AbrirOrdenPremarketCommand.of(player.getName(), nombreActivo, cantidadAComprar, LARGO_COMPRA, null),
                    () -> comprarLargoUseCase.comprarLargo(player.getName(), tipoActivo, nombreActivo, cantidadAComprar)
            );

            sendMessage(player, cantidadAComprar, executedInMarket);
        });
    }

    private void sendMessage(Player player, int cantidadAComprar, boolean executedInMarket) {
        if(executedInMarket){
            Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadAComprar + " " + tipoActivo.getAlias() + " de "
                    + nombreActivo + " a " + GREEN + FORMATEA.format(precioUnidad) + "PC");

            Funciones.enviarMensajeYSonido(player, GOLD + "Has comprado " + FORMATEA.format(cantidadAComprar)
                    + " cantidad a " + GREEN + FORMATEA.format(precioUnidad) + " PC" + GOLD + " que es un total de " + GREEN +
                    FORMATEA.format(precioUnidad * cantidadAComprar) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }else{
            player.sendMessage(GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara");
        }
    }

    @Override
    public double maxValue() {
        return dineroJugador / precioUnidad;
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " " + tipoActivo.getAlias(),
                GOLD + "Precio/Unidad: " + GREEN + FORMATEA.format(precioUnidad) + " PC",
                GOLD + "Precio/Total: " + GREEN + FORMATEA.format(precioUnidad * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + FORMATEA.format(dineroJugador) + " PC"
        );
    }
}
