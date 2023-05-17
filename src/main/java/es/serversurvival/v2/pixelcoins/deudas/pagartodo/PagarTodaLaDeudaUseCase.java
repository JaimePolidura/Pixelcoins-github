package es.serversurvival.v2.pixelcoins.deudas.pagartodo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas._shared.EstadoDeuda;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class PagarTodaLaDeudaUseCase {
    private final TransaccionesService transaccionesService;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    public void pagarTodaLaDeuda(UUID deudaId, UUID deudorId) {
        var deuda = this.deudasService.getById(deudaId);
        var pixelcoinsRestantes = deuda.getPixelcoinsTodasLasCuotasRestantes();

        asegurarseDeudorDeDeuda(deuda, deudorId);
        asegurarseDeudaPendiente(deuda);
        validador.jugadorTienePixelcoins(deudorId, pixelcoinsRestantes);

        deudasService.save(deuda.anotarPagadoPorCompleto());
        transaccionesService.save(Transaccion.builder()
                    .pagadoId(deuda.getAcredorJugadorId())
                    .pagadorId(deudorId)
                    .pixelcoins(pixelcoinsRestantes)
                    .objeto(deuda.getDeudaId().toString())
                    .tipo(TipoTransaccion.DEUDAS_PAGO_COMPLETO)
                .build());

        eventBus.publish(new DeudaPagadoPorCompleto(deudaId));
    }

    private void asegurarseDeudaPendiente(Deuda deuda) {
        if(deuda.getEstadoDeuda() != EstadoDeuda.PENDIENTE){
            throw new IllegalState("La deuda tiene que estar pendiente");
        }
    }

    private void asegurarseDeudorDeDeuda(Deuda deuda, UUID deudorId) {
        if(deuda.getDeudorJugadorId() != deudorId){
            throw new NotTheOwner("No eres deudor de esa deuda");
        }
    }
}
