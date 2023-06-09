package es.serversurvival.pixelcoins.jugadores.estadisticas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import es.serversurvival.pixelcoins.tienda.comprar.TiendaItemMinecraftVendido;
import lombok.AllArgsConstructor;

import static es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorTipoContadorEstadistica.*;

@EventHandler
@AllArgsConstructor
public final class OnCambioTienda {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;

    @EventListener
    public void on(TiendaItemMinecraftVendido evento) {
        jugadoresEstadisticasService.incrementar(evento.getCompradorJugadorId(), TIENDA_VALOR_COMPRAS, evento.getPixelcoins());
        jugadoresEstadisticasService.incrementar(evento.getVendedorJugadorId(), TIENDA_VALOR_VENTAS, evento.getPixelcoins());
    }
}
