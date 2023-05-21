package es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.TipoVotacion;
import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.iniciar.IniciarVotacionUseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ProponerNuevoDirectorUseCase {
    private final IniciarVotacionUseCase iniciarVotacionUseCase;
    private final EmpresasValidador empresasValidador;
    private final EventBus eventBus;

    public void proponer(ProponerNuevoDirectorParametros parametros) {
        empresasValidador.noDirectorEmpresa(parametros.getEmpresaId(), parametros.getNuevoDirectorId());
        empresasValidador.sueldoCorrecto(parametros.getSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getPeriodoPagoMs());

        iniciarVotacionUseCase.iniciar(CambiarDirectorVotacion.builder()
                .tipo(TipoVotacion.CAMBIAR_DIRECTOR)
                .nuevoDirectorJugadorId(parametros.getNuevoDirectorId())
                .empresaId(parametros.getEmpresaId())
                .descripccion(parametros.getDescripccion())
                .iniciadoPorJugadorId(parametros.getJugadorId())
                .periodoPagoMs(parametros.getPeriodoPagoMs())
                .sueldo(parametros.getSueldo())
                .build());

        eventBus.publish(new NuevoDirectorPropuesto(parametros));
    }
}
