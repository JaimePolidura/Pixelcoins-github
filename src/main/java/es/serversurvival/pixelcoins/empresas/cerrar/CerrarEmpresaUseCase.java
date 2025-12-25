package es.serversurvival.pixelcoins.empresas.cerrar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.RazonCierre;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class CerrarEmpresaUseCase implements UseCaseHandler<CerrarEmpresaParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final MovimientosService movimientosService;
    private final TransaccionesSaver transaccionesSaver;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EmpresasValidador validador;
    private final EventBus eventBus;

    @Override
    public void handle(CerrarEmpresaParametros parametros) {
        validador.empresaNoCerrada(parametros.getEmpresaId());
        validador.empresaNoCotizada(parametros.getEmpresaId());
        validador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());

        Empresa empresa = this.empresasService.getById(parametros.getEmpresaId());

        despedirATodosLosEmpleados(parametros.getEmpresaId());
        empresasService.save(empresa.cerrar(RazonCierre.CIERRE));
        accionistasEmpresasService.deleteByEmpresaId(parametros.getEmpresaId());
        transaccionesSaver.save(Transaccion.builder()
                .pagadoId(parametros.getJugadorId())
                .pagadorId(parametros.getEmpresaId())
                .pixelcoins(movimientosService.getBalance(parametros.getEmpresaId()))
                .tipo(TipoTransaccion.EMPRESAS_CERRAR)
                .build());

        eventBus.publish(new EmpresaCerrada(empresa));
    }

    private void despedirATodosLosEmpleados(UUID empresaId) {
        this.empleadosService.findEmpleoActivoByEmpresaId(empresaId).forEach(empleado -> {
            this.empleadosService.save(empleado.despedir("La empresa se ha cerrado"));
        });
    }
}
