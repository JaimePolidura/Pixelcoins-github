package es.serversurvival.minecraftserver.deudas.vendermercado;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.deudas.ponerventa.DeudaPuestaVenta;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnDeudaPuestaVenta {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void on(DeudaPuestaVenta e) {
        String vendedorNombre = jugadoresService.getNombreById(e.getPonerVentaDeudaParametros().getJugadorId());
        double precio = e.getPonerVentaDeudaParametros().getPrecio();

        enviadorMensajes.enviarMensaje(e.getPonerVentaDeudaParametros().getJugadorId(),GOLD + "Has puesto la deuda en el mercado por " +
                formatPixelcoins(precio) + "Para ver el mercado " + AQUA + " /deudas mercado");

        MinecraftUtils.broadcastExcept(e.getPonerVentaDeudaParametros().getJugadorId(), GOLD + vendedorNombre + " ha puesto deuda en el mercado por " + formatPixelcoins(precio));
    }
}
