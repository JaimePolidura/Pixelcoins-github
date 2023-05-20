package es.serversurvival.v2.pixelcoins.empresas.emitir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmitirAccionesServerUseCase {
    private final EmpresasValidador empresasValidador;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void emitir(EmitirAccionesServerUseCaseParametros parametros) {
        empresasValidador.numerAccionesValido(parametros.getNumeroNuevasAcciones());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.empresaCotizada(parametros.getEmpresaId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.precioPorAccion(parametros.getPrecioPorAccoin());

        Oferta oferta = OfertaAccionMercadoEmision.builder()
                .vendedorId(parametros.getEmpresaId())
                .precio(parametros.getPrecioPorAccoin())
                .cantidad(parametros.getNumeroNuevasAcciones())
                .objeto(parametros.getEmpresaId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_EMISION)
                .build();

        ofertasService.save(oferta);

        eventBus.publish(new AccionesEmitidasEmpresa(oferta.getOfertaId()));
    }
}
