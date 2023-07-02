package es.serversurvival.pixelcoins.empresas.editarempleado;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class EditarEmpleadoUseCase implements UseCaseHandler<EditarEmpleadoParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    @Override
    public void handle(EditarEmpleadoParametros parametros) {
        Empleado empleado = empleadosService.getById(parametros.getEmpleadoIdEdtiar());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empleadoJugadorEmpresaActivo(parametros.getEmpresaId(), empleado.getEmpleadoJugadorId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.sueldoCorrecto(parametros.getNuevoSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getNuevoPeriodoPagoMs());
        empresasValidador.descripccionCorrecta(parametros.getNuevaDescripccion());

        empleadosService.save(empleado.editar(parametros));

        eventBus.publish(new EmpleadoEditado(parametros.getEmpresaId(), empleado, parametros.getNuevaDescripccion(),
                parametros.getNuevoSueldo(), parametros.getNuevoPeriodoPagoMs()));
    }
}
