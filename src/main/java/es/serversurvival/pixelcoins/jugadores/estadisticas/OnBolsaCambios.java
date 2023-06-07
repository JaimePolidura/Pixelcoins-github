package es.serversurvival.pixelcoins.jugadores.estadisticas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.bolsa.abrir.PosicionBolsaAbierta;
import es.serversurvival.pixelcoins.bolsa.cerrar.PosicionBolsaCerrada;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.JugadorTipoContadorEstadistica;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.JugadoresEstadisticasService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnBolsaCambios {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;

    @EventListener
    public void on(PosicionBolsaAbierta evento) {
        jugadoresEstadisticasService.incrementar(evento.getJugadorId(), JugadorTipoContadorEstadistica.BOLSA_N_COMPRA_VENTAS);
    }

    @EventListener
    public void on(PosicionBolsaCerrada evento) {
        jugadoresEstadisticasService.incrementar(evento.getJugadorId(), JugadorTipoContadorEstadistica.BOLSA_N_COMPRA_VENTAS);
    }
}
