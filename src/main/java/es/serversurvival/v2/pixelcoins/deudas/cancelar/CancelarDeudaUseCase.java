package es.serversurvival.v2.pixelcoins.deudas.cancelar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas._shared.EstadoDeuda;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class CancelarDeudaUseCase {
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void cancelar(UUID deudaId, UUID acredorId) {
        Deuda deuda = deudasService.getById(deudaId);
        asegurarseAcredorDeDeuda(deuda, acredorId);
        asegurarseDeudaPendiente(deuda);

        deudasService.save(deuda.cancelar());

        eventBus.publish(new DeudaCancelada(deudaId));
    }

    private void asegurarseDeudaPendiente(Deuda deuda) {
        if(deuda.getEstadoDeuda() != EstadoDeuda.PENDIENTE){
            throw new IllegalState("La deuda tiene que estar pendiente");
        }
    }

    private void asegurarseAcredorDeDeuda(Deuda deuda, UUID acredorId) {
        if(deuda.getAcredorJugadorId() != acredorId){
            throw new NotTheOwner("No eres el acredor de la deuda");
        }
    }
}
