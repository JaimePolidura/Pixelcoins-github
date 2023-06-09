package es.serversurvival.pixelcoins.empresas.dejarempleo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DejarEmploUseCase implements UseCaseHandler<DejarEmploParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    @Override
    public void handle(DejarEmploParametros parametros) {
        empresasValidador.empleadoEmpresaActivo(parametros.getEmpresaId(), parametros.getJugadorId());

        Empleado empleao = empleadosService.findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(parametros.getEmpresaId(), parametros.getJugadorId())
                .get();

        empleadosService.deleteById(empleao.getEmpleadoId());

        eventBus.publish(new JugadorDejoEmpleo(parametros.getJugadorId(), parametros.getEmpresaId()));
    }
}
