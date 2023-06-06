package es.serversurvival.v2.pixelcoins.empresas.emitiracciones;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmitirAccionesServerUseCase {
    private final EmpresasValidador empresasValidador;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void emitir(EmitirAccionesServerParametros parametros) {
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
                .objeto(parametros.getEmpresaId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_EMISION)
                .build());

        eventBus.publish(new AccionesEmitidasEmpresa(parametros));
    }
}
