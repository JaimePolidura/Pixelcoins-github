package es.serversurvival.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.DecrementorPosicionesAccionesJugador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaAccionMercadoJugadorCompradaListener implements OfertaCompradaListener<OfertaAccionMercadoJugador> {
    private final DecrementorPosicionesAccionesJugador decrementorPosicionesAccionesJugador;
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final VotosService votosService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoJugador ofertaComprada, UUID compradorId) {
        UUID empresaId = ofertaComprada.getObjetoToUUID();
        UUID vendedorId = ofertaComprada.getEmpresaId();

        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);
        decrementorPosicionesAccionesJugador.decrementarEnUno(vendedorId, empresaId);

        eventBus.publish(new AccionServerComprada(empresaId, compradorId, ofertaComprada.getPrecio()));
    }
}
