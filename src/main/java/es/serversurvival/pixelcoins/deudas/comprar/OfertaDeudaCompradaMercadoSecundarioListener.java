package es.serversurvival.pixelcoins.deudas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaDeudaCompradaMercadoSecundarioListener implements OfertaCompradaListener<OfertaDeudaMercadoSecundario> {
    private final DeudasService deudasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaDeudaMercadoSecundario ofertaComprada, UUID compradorJugadorId) {
        UUID deudaId = ofertaComprada.getObjetoToUUID();
        Deuda deuda = deudasService.getById(deudaId);

        deudasService.save(deuda.nuevoAcredor(compradorJugadorId));

        eventBus.publish(new DeudaComprada(deudaId, compradorJugadorId, ofertaComprada.getPrecio()));
    }
}
