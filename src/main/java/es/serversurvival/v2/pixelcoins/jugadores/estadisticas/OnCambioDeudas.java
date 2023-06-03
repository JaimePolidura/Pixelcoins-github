package es.serversurvival.v2.pixelcoins.jugadores.estadisticas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v2.pixelcoins.deudas.pagarcuotas.CuotaDeudaNoPagadaEvento;
import es.serversurvival.v2.pixelcoins.deudas.pagarcuotas.CuotaDeudaPagadaEvento;
import es.serversurvival.v2.pixelcoins.deudas.pagartodo.DeudaPagadoPorCompleto;
import es.serversurvival.v2.pixelcoins.jugadores._shared.estadisticas.JugadorTipoContadorEstadistica;
import es.serversurvival.v2.pixelcoins.jugadores._shared.estadisticas.JugadoresEstadisticasService;
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
