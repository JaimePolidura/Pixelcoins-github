package es.serversurvival.pixelcoins.empresas.despedir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DespedirEmpleadoUseCase implements UseCaseHandler<DespedirEmpleadoParametros> {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(DespedirEmpleadoParametros parametros) {
        validador.stringLongitudEntre(parametros.getCausa(), 1, 16, "causa de despido");
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        Empleado empleado = this.empleadosService.getById(parametros.getEmpleadoIdADespedir());
        empresasValidador.empleadoJugadorEmpresaActivo(parametros.getEmpresaId(), empleado.getEmpleadoJugadorId());
        empresasValidador.empleadoNoEsDirector(parametros.getEmpresaId(), empleado.getEmpleadoJugadorId());

        empleadosService.save(empleado.despedir(parametros.getCausa()));

        eventBus.publish(new EmpleadoDespedido(parametros.getEmpleadoIdADespedir()));
    }
}
