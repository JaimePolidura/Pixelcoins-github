package es.serversurvival.pixelcoins.empresas.contratar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ContratarEmpleadoUseCase implements UseCaseHandler<ContratarEmpleadoParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    @Override
    public void handle(ContratarEmpleadoParametros parametros) {
        validar(parametros);

        empleadosService.save(Empleado.fromContratarParametros(parametros));

        eventBus.publish(new JugadorContratado(parametros.getJugadorIdAContratar(), parametros.getJugadorId(),
                parametros.getEmpresaId()));
    }

    public void validar(ContratarEmpleadoParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.noEmpleadoEmpresa(parametros.getEmpresaId(), parametros.getJugadorIdAContratar());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.sueldoCorrecto(parametros.getSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getPeriodoPagoMs());
        empresasValidador.descripccionCorrecta(parametros.getDescripccion());
    }
}
