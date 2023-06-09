package es.serversurvival.pixelcoins.jugadores.estadisticas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaDeudaNoPagadaEvento;
import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaDeudaPagadaEvento;
import es.serversurvival.pixelcoins.deudas.pagartodo.DeudaPagadoPorCompleto;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorTipoContadorEstadistica;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnCambioDeudas {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;

    @EventListener
    public void on(DeudaPagadoPorCompleto evento) {
        jugadoresEstadisticasService.incrementar(evento.getDeudorJugadorId(), JugadorTipoContadorEstadistica.N_DEUDA_PAGOS);
    }

    @EventListener
    public void on(CuotaDeudaPagadaEvento evento) {
        jugadoresEstadisticasService.incrementar(evento.getDeudorJugadorId(), JugadorTipoContadorEstadistica.N_DEUDA_PAGOS);
    }

    @EventListener
    public void on(CuotaDeudaNoPagadaEvento evento) {
        jugadoresEstadisticasService.incrementar(evento.getDeudorJugadorId(), JugadorTipoContadorEstadistica.N_DEUDA_INPAGOS);
    }
}
