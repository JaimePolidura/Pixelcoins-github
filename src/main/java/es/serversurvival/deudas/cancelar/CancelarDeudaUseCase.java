package es.serversurvival.deudas.cancelar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarDeudaUseCase {
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public CancelarDeudaUseCase () {
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public Deuda cancelarDeuda(String playerName, UUID deudaId) {
        var deudaACancelar = this.deudasService.getById(deudaId);
        this.ensureIsAcredor(deudaACancelar, playerName);

        this.deudasService.deleteById(deudaId);

        this.eventBus.publish(new DeudaCanceladaEvento(playerName, deudaACancelar.getDeudor(), deudaACancelar.getPixelcoinsRestantes()));

        return deudaACancelar;
    }


    private void ensureIsAcredor(Deuda deuda, String playerName){
        if(!deuda.getAcredor().equals(playerName))
            throw new NotTheOwner("No eres el acredor de la deuda");
    }
}
