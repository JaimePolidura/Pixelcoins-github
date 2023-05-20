package es.serversurvival.v2.pixelcoins.empresas.despedir;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class DespedirEmpleadoUseCase  {
    private final EmpresasValidador empresasValidador;
    private final EmpleadosService empleadosService;
    private final Validador validador;
    private final EventBus eventBus;

    public void despedir(DespedirEmpleadoParametros parametros) {
        validador.stringLongitudEntre(parametros.getCausa(), 1, 16, "causa de despido");
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorIdQueDespide());
        empresasValidador.empleadoEmpresaActivo(parametros.getEmpresaId(), parametros.getEmpleadoIdADespedir());
        empresasValidador.empleadoNoEsDirector(parametros.getEmpresaId(), parametros.getEmpleadoIdADespedir());

        Empleado empleado = this.empleadosService.getById(parametros.getEmpleadoIdADespedir());

        empleadosService.save(empleado.despedir(parametros.getCausa()));

        eventBus.publish(new EmpleadoDespedido(parametros.getEmpleadoIdADespedir()));
    }
}
