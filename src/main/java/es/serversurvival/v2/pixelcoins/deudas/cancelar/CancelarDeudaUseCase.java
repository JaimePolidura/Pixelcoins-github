package es.serversurvival.v2.pixelcoins.deudas.cancelar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasValidador;
import es.serversurvival.v2.pixelcoins.deudas._shared.EstadoDeuda;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class CancelarDeudaUseCase {
    private final DeudasValidador deudasValidador;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void cancelar(UUID deudaId, UUID acredorId) {
        deudasValidador.acredorDeDeuda(deudaId, acredorId);
        deudasValidador.deudaPendiente(deudaId);

        Deuda deuda = deudasService.getById(deudaId);

        deudasService.save(deuda.cancelar());
        ofertasService.deleteByObjetoYTipo(deuda.getDeudaId().toString(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);

        eventBus.publish(new DeudaCancelada(deudaId));
    }
}
