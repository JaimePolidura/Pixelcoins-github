package es.serversurvival.v1.bolsa.activosinfo.vervalores;

import es.serversurvival.v1._shared.menus.NumberSelectorMenu;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.v1.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@AllArgsConstructor
public final class ComprarBolsaConfirmacionMenu extends NumberSelectorMenu<ComprarBolsaConfirmacionMenuState> {
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final OrderExecutorProxy orderExecutorProxy;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @Override
    public void onAccept(Player player, double cantidadAComprarDouble, InventoryClickEvent event) {
        Funciones.POOL.submit(() -> {
            int cantidadAComprar = (int) cantidadAComprarDouble;
            double pixelcoinsJugador = this.jugadoresService.getByNombre(player.getName()).getPixelcoins();

            if (pixelcoinsJugador < (cantidadAComprar * getState().precioUnidad())) {
                player.sendMessage(ChatColor.DARK_RED + "No tienes el suficiente dinero");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                return;
            }

            var executedInMarket = orderExecutorProxy.execute(
                    AbrirOrdenPremarketCommand.of(player.getName(), getState().nombreActivo(), cantidadAComprar, TipoAccion.LARGO_COMPRA, null),
                    () -> comprarLargoUseCase.comprarLargo(player.getName(), getState().tipoActivo(), getState().nombreActivo(), cantidadAComprar)
            );

            sendMessage(player, cantidadAComprar, executedInMarket);
        });
    }

    private void sendMessage(Player player, int cantidadAComprar, boolean executedInMarket) {
        if(executedInMarket){
            Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadAComprar + " " + getState().tipoActivo().getAlias() + " de "
                    + getState().nombreActivo() + " a " + GREEN + Funciones.FORMATEA.format(getState().precioUnidad()) + "PC");

            enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Has comprado " + Funciones.FORMATEA.format(cantidadAComprar)
                    + " cantidad a " + GREEN + Funciones.FORMATEA.format(getState().precioUnidad()) + " PC" + GOLD + " que es un total de " + GREEN +
                    Funciones.FORMATEA.format(getState().precioUnidad() * cantidadAComprar) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa cartera", ENTITY_PLAYER_LEVELUP);
        }else{
            player.sendMessage(GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara");
        }
    }

    @Override
    public double maxValue() {
        return getState().jugador().getPixelcoins() / getState().precioUnidad();
    }

    @Override
    public double initialValue() {
        return 1;
    }

    @Override
    public List<String> loreItemAceptar(double cantidad) {
        return List.of(
                GOLD + "Comprar: " + cantidad + " " + getState().tipoActivo().getAlias(),
                GOLD + "Precio/Unidad: " + GREEN + Funciones.FORMATEA.format(getState().precioUnidad()) + " PC",
                GOLD + "Precio/Total: " + GREEN + Funciones.FORMATEA.format(getState().precioUnidad() * cantidad) + " PC",
                GOLD + "Tus pixelcoins: " + GREEN + Funciones.FORMATEA.format(getState().jugador().getPixelcoins()) + " PC"
        );
    }
}
