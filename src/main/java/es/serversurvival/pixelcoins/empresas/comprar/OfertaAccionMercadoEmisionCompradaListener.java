package es.serversurvival.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaAccionMercadoEmisionCompradaListener implements OfertaCompradaListener<OfertaAccionMercadoEmision> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoEmision ofertaComprada, UUID compradorId) {
        UUID empresaId = ofertaComprada.getObjetoToUUID();
        Empresa empresa = empresasService.getById(empresaId);

        empresasService.save(empresa.incrementNTotalAccionesEn(1));
        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);

        eventBus.publish(new AccionServerComprada(empresaId, empresa.getDirectorJugadorId(), compradorId,
                ofertaComprada.getPrecio(), TipoOferta.ACCIONES_SERVER_EMISION, ofertaComprada.getVendedorId()));
    }
}
