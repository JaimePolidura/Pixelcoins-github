package es.serversurvival.v2.pixelcoins.deudas.comprar.secundario;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas.comprar.DeudaComprada;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaCompradaListener;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaDeudaCompradaMercadoSecundarioListener implements CustomOfertaCompradaListener<OfertaDeudaMercadoSecundario> {
    private final DeudasService deudasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaDeudaMercadoSecundario ofertaComprada, UUID compradorJugadorId) {
        UUID deudaId = ofertaComprada.objetoToUUID();
        Deuda deuda = deudasService.getById(deudaId);

        deudasService.save(deuda.nuevoAcredor(compradorJugadorId));

        eventBus.publish(new DeudaComprada(deudaId, compradorJugadorId, ofertaComprada.getPrecio()));
    }
}
