package es.serversurvival.v2.pixelcoins.empresas.ipo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EmpresasIPOUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void ipo(EmpresaIPOParametros parametros) {
        validar(parametros);

        AccionistaEmpresa acciones = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());

        ofrecerOfertaUseCase.ofrecer(OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getEmpresaId())
                .precio(parametros.getPrecioPorAccion())
                .cantidad(parametros.getNumeroAccionesVender())
                .empresaId(parametros.getEmpresaId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_JUGADOR)
                .empresaId(parametros.getEmpresaId())
                .objeto(acciones.getAccionistaId())
                .build());

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
