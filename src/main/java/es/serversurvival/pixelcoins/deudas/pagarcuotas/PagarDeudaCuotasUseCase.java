package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival._shared.TiempoService;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.transacciones.*;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import lombok.AllArgsConstructor;

import static es.jaime.javaddd.application.utils.Utils.*;

@UseCase
@AllArgsConstructor
public final class PagarDeudaCuotasUseCase implements UseCaseHandler<PagarDeudaCuotasParametros> {
    private final TransaccionesBalanceService transaccionesBalanceService;
    private final TransaccionesSaver transaccionesSaver;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final TiempoService tiempoService;
    private final EventBus eventBus;

    @Override
    public void handle(PagarDeudaCuotasParametros parametros) {
        repeat(getNumeroCuotasPendientesDePagar(parametros.getDeuda()), () ->  {
            pagarCuota(parametros.getDeuda());
        });
    }

    private void pagarCuota(Deuda deuda) {
        boolean deudorTienePixelcoins = transaccionesBalanceService.get(deuda.getDeudorJugadorId()) >= deuda.getCuota();

        if(!deudorTienePixelcoins){
            anotarImpagoDeudaCuota(deuda);
            return;
        }

        transaccionesSaver.save(Transaccion.builder()
                .pagadorId(deuda.getDeudorJugadorId())
                .pagadoId(deuda.getAcredorJugadorId())
                .pixelcoins(deuda.getCuota())
                .tipo(TipoTransaccion.DEUDAS_CUOTA)
                .objeto(deuda.getDeudaId().toString())
                .build());
        deudasService.save(deuda.anotarPagoCuota());

        if(!deuda.estaPendiente()){
            ofertasService.deleteByObjetoYTipo(deuda.getDeudaId().toString(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);
        }

        eventBus.publish(new CuotaDeudaPagadaEvento(deuda.getDeudaId(), deuda.getCuota(), deuda.getDeudorJugadorId()));
    }

    private void anotarImpagoDeudaCuota(Deuda deuda) {
        deudasService.save(deuda.incrementarNImpago());
        eventBus.publish(new CuotaDeudaNoPagadaEvento(deuda.getDeudaId(), deuda.getCuota(), deuda.getDeudorJugadorId()));
    }

    private int getNumeroCuotasPendientesDePagar(Deuda deuda) {
        long periodoPagoCuotaMs = deuda.getPeriodoPagoCuotaMs();
        long hoyMs = tiempoService.millis();
        long ulitmoPagoMs = tiempoService.toMillis(deuda.getFechaUltimoPagoCuota());

        long tiempoTranscurriendoTrasUltimoPago = hoyMs - ulitmoPagoMs;

        return (int) (tiempoTranscurriendoTrasUltimoPago / periodoPagoCuotaMs);
    }
}
