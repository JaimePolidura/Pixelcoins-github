package es.serversurvival.v2.pixelcoins.deudas.pagartodo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasValidador;
import es.serversurvival.v2.pixelcoins.deudas._shared.EstadoDeuda;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class PagarTodaLaDeudaUseCase {
    private final TransaccionesService transaccionesService;
    private final DeudasValidador deudasValidador;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    public void pagarTodaLaDeuda(UUID deudaId, UUID deudorId) {
        var deuda = this.deudasService.getById(deudaId);
        var pixelcoinsRestantes = deuda.getPixelcoinsTodasLasCuotasRestantes();

        deudasValidador.deudorDeDeuda(deudaId, deudorId);
        deudasValidador.deudaPendiente(deudaId);
        validador.jugadorTienePixelcoins(deudorId, deuda.getPixelcoinsTodasLasCuotasRestantes());

        deudasService.save(deuda.anotarPagadoPorCompleto());
        ofertasService.deleteByObjetoYTipo(deudaId.toString(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);
        transaccionesService.save(Transaccion.builder()
                    .pagadoId(deuda.getAcredorJugadorId())
                    .pagadorId(deudorId)
                    .pixelcoins(pixelcoinsRestantes)
                    .objeto(deuda.getDeudaId().toString())
                    .tipo(TipoTransaccion.DEUDAS_PAGO_COMPLETO)
                .build());

        eventBus.publish(new DeudaPagadoPorCompleto(deudaId));
    }
}
