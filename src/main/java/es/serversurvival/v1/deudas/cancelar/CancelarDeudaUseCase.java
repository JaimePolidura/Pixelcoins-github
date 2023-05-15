package es.serversurvival.v1.deudas.cancelar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.deudas._shared.domain.Deuda;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@UseCase
public final class CancelarDeudaUseCase {
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void cancelarDeuda(String playerName, UUID deudaId) {
        var deudaACancelar = this.deudasService.getById(deudaId);
        this.ensureIsAcredor(deudaACancelar, playerName);

        this.deudasService.deleteById(deudaId);

        this.eventBus.publish(new DeudaCanceladaEvento(playerName, deudaACancelar.getDeudor(), deudaACancelar.getPixelcoinsRestantes()));
    }

    private void ensureIsAcredor(Deuda deuda, String playerName){
        if(!deuda.getAcredor().equals(playerName))
            throw new NotTheOwner("No eres el acredor de la deuda");
    }
}
