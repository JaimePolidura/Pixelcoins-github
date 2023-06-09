package es.serversurvival.pixelcoins.empresas.irseempleo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DejarEmpleoUseCase implements UseCaseHandler<DejarEmpleoParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    @Override
    public void handle(DejarEmpleoParametros parametros) {
        empresasValidador.empleadoEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());

        Empleado empleado = empleadosService.getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());

        empleadosService.deleteById(empleado.getEmpleadoId());

        eventBus.publish(new EmpleadoDejoEmpresa(parametros.getEmpresaId(), parametros.getJugadorId()));
    }
}
