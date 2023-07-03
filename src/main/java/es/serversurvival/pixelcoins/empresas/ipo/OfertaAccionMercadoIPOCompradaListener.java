package es.serversurvival.pixelcoins.empresas.ipo;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.DecrementorPosicionesAccionesJugador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.comprar.AccionServerComprada;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaAccionMercadoIPOCompradaListener implements OfertaCompradaListener<OfertaAccionMercadoIPO> {
    private final DecrementorPosicionesAccionesJugador decrementorPosicionesAccionesJugador;
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoIPO ofertaComprada, UUID compradorId) {
        UUID empresaId = ofertaComprada.getEmpresaId();
        Empresa empresa = empresasService.getById(empresaId);
        UUID accionesId = ofertaComprada.getObjetoToUUID();

        AccionistaEmpresa accionesAVender = accionistasEmpresasService.getById(accionesId);

        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);
        decrementorPosicionesAccionesJugador.decrementarEnUno(accionesAVender);

        eventBus.publish(new AccionServerComprada(empresaId, empresa.getDirectorJugadorId(), compradorId,
                ofertaComprada.getPrecio(), TipoOferta.ACCIONES_SERVER_IPO, ofertaComprada.getVendedorId()));
    }
}
