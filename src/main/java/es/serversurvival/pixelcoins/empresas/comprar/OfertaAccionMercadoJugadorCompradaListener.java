package es.serversurvival.pixelcoins.empresas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
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
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void on(OfertaAccionMercadoJugador ofertaComprada, UUID compradorId) {
        UUID accionesId = ofertaComprada.getObjetoToUUID();
        UUID empresaId = ofertaComprada.getEmpresaId();
        Empresa empresa = empresasService.getById(empresaId);

        AccionistaEmpresa accionesAVender = accionistasEmpresasService.getById(accionesId);

        accionistasEmpresasService.incrementarPosicionAccionEnUno(empresaId, compradorId);
        decrementorPosicionesAccionesJugador.decrementarEnUno(accionesAVender);

        eventBus.publish(new AccionServerComprada(empresa.getEmpresaId(), empresa.getDirectorJugadorId(), compradorId,
                ofertaComprada.getPrecio(), TipoOferta.ACCIONES_SERVER_JUGADOR, ofertaComprada.getVendedorId()));
    }
}
