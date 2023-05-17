package es.serversurvival.v2.pixelcoins.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import static es.jaime.javaddd.application.utils.Utils.*;
import static es.serversurvival.v1._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class PagadorDeudaCuotas {
    private final TransaccionesService transaccionesService;
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void pagarCuotas(Deuda deuda) {
        int cuotasPendientesDePagar = getCuotasPendientesDePagar(deuda);

        if(cuotasPendientesDePagar == 0){
            return;
        }

        repeat(cuotasPendientesDePagar, () ->  {
            pagarCuota(deuda);
        });
    }

    private void pagarCuota(Deuda deuda) {
        boolean deudorTienePixelcoins = transaccionesService.getBalancePixelcions(deuda.getDeudorJugadorId()) >= deuda.getCuota();

        if(!deudorTienePixelcoins){
            anotarImpagoDeudaCuota(deuda);
            return;
        }

        deudasService.save(deuda.anotarPagoCuota());
        transaccionesService.save(Transaccion.builder()
                .pagadorId(deuda.getDeudorJugadorId())
                .pagadoId(deuda.getAcredorJugadorId())
                .pixelcoins(deuda.getCuota())
                .tipo(TipoTransaccion.DEUDAS_CUOTA)
                .objeto(deuda.getDeudaId().toString())
                .build());

        eventBus.publish(new CuotaDeudaNoPagadaEvento(deuda.getDeudaId(), deuda.getCuota()));
    }

    private void anotarImpagoDeudaCuota(Deuda deuda) {
        deudasService.save(deuda.incrementarNImpago());
        eventBus.publish(new CuotaDeudaNoPagadaEvento(deuda.getDeudaId(), deuda.getCuota()));
    }

    private int getCuotasPendientesDePagar(Deuda deuda) {
        long periodoPagoCuotaMs = deuda.getPeriodoPagoCuotaMs();
        long hoyMs = System.currentTimeMillis();
        long ulitmoPagoMs = toMillis(deuda.getFechaUltimoPagoCuota());

        long tiempoTranscurriendoTrasUltimoPago = hoyMs - ulitmoPagoMs;

        return (int) (tiempoTranscurriendoTrasUltimoPago / periodoPagoCuotaMs);
    }
}
