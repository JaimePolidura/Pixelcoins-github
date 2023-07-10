package es.serversurvival.pixelcoins.empresas.ipo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaParametros;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaUseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmpresasIPOUseCase implements UseCaseHandler<EmpresaIPOParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final PonerVentaOfertaUseCase ofrecerOfertaUseCase;
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    @Override
    public void handle(EmpresaIPOParametros parametros) {
        validar(parametros);

        AccionistaEmpresa acciones = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());

        ofrecerOfertaUseCase.handle(PonerVentaOfertaParametros.of(OfertaAccionMercadoIPO.builder()
                .accionesFundadorId(acciones.getAccionistaId())
                .empresaId(parametros.getEmpresaId())
                .cantidad(parametros.getNumeroAccionesVender())
                .precio(parametros.getPrecioPorAccion())
                .vendedorId(acciones.getEmpresaId())
                .build()));

        empresasService.save(empresasService.getById(parametros.getEmpresaId()).marcarComoCotizada());

        eventBus.publish(new EmpresaIPORealizada(parametros.getJugadorId(), parametros.getEmpresaId(), parametros.getNumeroAccionesVender(),
                parametros.getPrecioPorAccion()));
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
