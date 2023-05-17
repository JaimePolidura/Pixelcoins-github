package es.serversurvival.v2.pixelcoins.empresas.contratar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ContratarEmpleadoUseCase {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void contratar(ContratarEmpleadoUseCaseParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.noEmpleadoEmpresa(parametros.getEmpresaId(), parametros.getJugadorIdAContratar());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorIdContrador());
        empresasValidador.sueldoCorrecto(parametros.getSueldo());
        empresasValidador.periodoPagoCorrecto(parametros.getPeriodoPagoMs());
        empresasValidador.descripccionCorrecta(parametros.getDescripccion());

        empleadosService.save(Empleado.fromContratarParametros(parametros));

        eventBus.publish(new JugadorContratado(parametros.getJugadorIdAContratar()));
    }
}
