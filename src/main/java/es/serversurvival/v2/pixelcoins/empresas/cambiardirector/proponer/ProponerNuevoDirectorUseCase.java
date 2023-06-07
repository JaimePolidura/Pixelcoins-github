package es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votaciones.TipoVotacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionParametros;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionUseCase;
import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.iniciar.IniciarVotacionUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class ProponerNuevoDirectorUseCase {
    private final IniciarVotacionUseCase iniciarVotacionUseCase;
    private final VotarVotacionUseCase votarVotacionUseCase;
    private final EmpresasValidador empresasValidador;
    private final EventBus eventBus;

    public void proponer(ProponerNuevoDirectorParametros parametros) {
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
