package es.serversurvival.pixelcoins.empresas.ipo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado.ofrecer.OfrecerOfertaParametros;
import es.serversurvival.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmpresasIPOUseCase implements UseCaseHandler<EmpresaIPOParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void handle(EmpresaIPOParametros parametros) {
        validar(parametros);

        AccionistaEmpresa acciones = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());

        ofrecerOfertaUseCase.handle(OfrecerOfertaParametros.of(OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getEmpresaId())
                .precio(parametros.getPrecioPorAccion())
                .cantidad(parametros.getNumeroAccionesVender())
                .empresaId(parametros.getEmpresaId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_JUGADOR)
                .empresaId(parametros.getEmpresaId())
                .objeto(acciones.getAccionistaId())
                .build()));

        empresasService.save(empresasService.getById(parametros.getEmpresaId()).marcarComoCotizada());

        eventBus.publish(new EmpresaIPORealizada(parametros));
    }

    public void validar(EmpresaIPOParametros parametros) {
        empresasValidador.numerAccionesValido(parametros.getNumeroAccionesVender());
        empresasValidador.precioPorAccion(parametros.getPrecioPorAccion());
        empresasValidador.empresaNoCotizada(parametros.getEmpresaId());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.jugadorTieneAcciones(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getNumeroAccionesVender());
    }
}
