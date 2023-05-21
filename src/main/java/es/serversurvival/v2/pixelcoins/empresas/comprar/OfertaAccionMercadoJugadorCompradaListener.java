package es.serversurvival.v2.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.DecrementorPosicionesAccionesJugador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos.VotosService;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
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
