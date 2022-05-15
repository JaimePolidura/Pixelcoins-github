package es.serversurvival.deudas.cancelar;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;

import java.util.UUID;

public final class CancelarDeudaUseCase {
    private final DeudasService deudasService;

    public CancelarDeudaUseCase () {
        this.deudasService = DependecyContainer.get(DeudasService.class);
    }

    public Deuda cancelarDeuda(String playerName, UUID deudaId) {
        Deuda deudaACancelar = ensureDeudaExist(deudaId);
        this.ensureIsAcredor(deudaACancelar, playerName);

        this.deudasService.deleteById(deudaId);

        Pixelcoin.publish(new DeudaCanceladaEvento(playerName, deudaACancelar.getDeudor(), deudaACancelar.getPixelcoins_restantes()));

        return deudaACancelar;
    }

    private Deuda ensureDeudaExist(UUID deudaId){
        return this.deudasService.getDeudaById(deudaId);
    }

    private void ensureIsAcredor(Deuda deuda, String playerName){
        if(!deuda.getAcredor().equals(playerName))
            throw new NotTheOwner("No eres el acredor de la deuda");
    }
}
