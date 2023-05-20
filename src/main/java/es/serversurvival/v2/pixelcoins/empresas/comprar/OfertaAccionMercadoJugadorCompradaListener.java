package es.serversurvival.v2.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaCompradaListener;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaAccionMercadoJugadorCompradaListener implements CustomOfertaCompradaListener<OfertaAccionMercadoJugador> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoJugador ofertaComprada, UUID compradorId) {
        UUID empresaId = ofertaComprada.getObjetoToUUID();
        UUID vendedorId = ofertaComprada.getAccionistaJugadorId();

        incrementarPosicionAccionComprador(compradorId, empresaId);
        decrementarPosicionAccionVendedor(vendedorId, empresaId);

        eventBus.publish(new AccionServerComprada(empresaId, compradorId, ofertaComprada.getPrecio()));
    }

    private void decrementarPosicionAccionVendedor(UUID vendedorId, UUID empresaId) {
        AccionistaEmpresa accionistaServer = accionistasEmpresasService.getByEmpresaIdAndJugadorId(empresaId, vendedorId)
                .decrementarNAccionesPorUno();

        if(accionistaServer.noTieneMasAcciones()){
            accionistasEmpresasService.deleteById(accionistaServer.getAccionistaId());
        }else{
            accionistasEmpresasService.save(accionistaServer);
        }
    }

    private void incrementarPosicionAccionComprador(UUID compradorId, UUID empresaId) {
        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);
    }
}
