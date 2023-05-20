package es.serversurvival.v2.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaCompradaListener;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaAccionMercadoEmisionCompradaListener implements CustomOfertaCompradaListener<OfertaAccionMercadoEmision> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoEmision ofertaComprada, UUID compradorId) {
        UUID empresaId = ofertaComprada.getObjetoToUUID();
        Empresa empresa = empresasService.getById(empresaId);

        empresasService.save(empresa.incrementNTotalAccionesEn(1));
        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);

        eventBus.publish(new AccionServerComprada(empresaId, compradorId, ofertaComprada.getPrecio()));
    }
}
