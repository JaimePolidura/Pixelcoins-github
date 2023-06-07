package es.serversurvival.pixelcoins.empresas.dejarempleo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DejarEmploUseCase {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void dejar(DejarEmploParametros parametros) {
        empresasValidador.empleadoEmpresaActivo(parametros.getEmpresaId(), parametros.getJugadorId());

        Empleado empleao = empleadosService.findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(parametros.getEmpresaId(), parametros.getJugadorId())
                .get();

        empleadosService.deleteById(empleao.getEmpleadoId());

        eventBus.publish(new JugadorDejoEmpleo(parametros.getJugadorId(), parametros.getEmpresaId()));
    }
}
