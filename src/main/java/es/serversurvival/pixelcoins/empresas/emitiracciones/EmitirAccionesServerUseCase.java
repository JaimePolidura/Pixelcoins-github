package es.serversurvival.pixelcoins.empresas.emitiracciones;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmitirAccionesServerUseCase implements UseCaseHandler<EmitirAccionesServerParametros> {
    private final EmpresasValidador empresasValidador;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    @Override
    public void handle(EmitirAccionesServerParametros parametros) {
        empresasValidador.numerAccionesValido(parametros.getNumeroNuevasAcciones());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.empresaCotizada(parametros.getEmpresaId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.precioPorAccion(parametros.getPrecioPorAccion());

        ofertasService.save(OfertaAccionMercadoEmision.builder()
                .vendedorId(parametros.getEmpresaId())
                .precio(parametros.getPrecioPorAccion())
                .cantidad(parametros.getNumeroNuevasAcciones())
                .empresaId(parametros.getEmpresaId())
                .build());

        eventBus.publish(new AccionesEmitidasEmpresa(parametros));
    }
}
