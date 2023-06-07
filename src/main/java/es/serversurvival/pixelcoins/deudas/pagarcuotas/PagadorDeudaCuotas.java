package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.DeudasService;
import lombok.AllArgsConstructor;

import static es.jaime.javaddd.application.utils.Utils.*;
import static es.serversurvival._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class PagadorDeudaCuotas {
    private final TransaccionesService transaccionesService;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void pagarCuotas(Deuda deuda) {
        repeat(getNumeroCuotasPendientesDePagar(deuda), () ->  {
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
        long hoyMs = System.currentTimeMillis();
        long ulitmoPagoMs = toMillis(deuda.getFechaUltimoPagoCuota());

        long tiempoTranscurriendoTrasUltimoPago = hoyMs - ulitmoPagoMs;

        return (int) (tiempoTranscurriendoTrasUltimoPago / periodoPagoCuotaMs);
    }
}
