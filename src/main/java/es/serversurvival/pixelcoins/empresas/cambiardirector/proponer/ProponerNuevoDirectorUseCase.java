package es.serversurvival.pixelcoins.empresas.cambiardirector.proponer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionParametros;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionUseCase;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar.IniciarVotacionUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class ProponerNuevoDirectorUseCase implements UseCaseHandler<ProponerNuevoDirectorParametros> {
    private final IniciarVotacionUseCase iniciarVotacionUseCase;
    private final VotarVotacionUseCase votarVotacionUseCase;
    private final EmpresasValidador empresasValidador;
    private final EventBus eventBus;

    @Override
    public void handle(ProponerNuevoDirectorParametros parametros) throws Exception {
        empresasValidador.noDirectorEmpresa(parametros.getEmpresaId(), parametros.getNuevoDirectorId());
        empresasValidador.sueldoCorrecto(parametros.getSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getPeriodoPagoMs());

        UUID votacionId = iniciarVotacionUseCase.iniciar(CambiarDirectorVotacion.builder()
                .tipo(TipoVotacion.CAMBIAR_DIRECTOR)
                .nuevoDirectorJugadorId(parametros.getNuevoDirectorId())
                .empresaId(parametros.getEmpresaId())
                .descripccion(parametros.getDescripccion())
                .iniciadoPorJugadorId(parametros.getJugadorId())
                .periodoPagoMs(parametros.getPeriodoPagoMs())
                .sueldo(parametros.getSueldo())
                .build());

        votarVotacionUseCase.votar(VotarVotacionParametros.builder()
                .aFavor(true)
                .votacionId(votacionId)
                .jugadorId(parametros.getJugadorId())
                .empresaId(parametros.getEmpresaId())
                .build());

        eventBus.publish(new NuevoDirectorPropuesto(parametros));
    }
}
