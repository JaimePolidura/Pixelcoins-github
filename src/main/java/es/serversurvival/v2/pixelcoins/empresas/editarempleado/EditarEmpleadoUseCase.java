package es.serversurvival.v2.pixelcoins.empresas.editarempleado;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EditarEmpleadoUseCase {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void editar(EditarEmpleadoParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empleadoEmpresaActivo(parametros.getEmpresaId(), parametros.getEmpleadoIdEdtiar());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.sueldoCorrecto(parametros.getNuevoSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getNuevoPeriodoPago());
        empresasValidador.descripccionCorrecta(parametros.getNuevaDescripccion());

        Empleado empleado = empleadosService.getById(parametros.getEmpleadoIdEdtiar());
        empleadosService.save(empleado.editar(parametros));

        eventBus.publish(new EmpleadoEditado(parametros.getEmpleadoIdEdtiar()));
    }
}
