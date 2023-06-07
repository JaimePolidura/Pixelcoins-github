package es.serversurvival.pixelcoins.empresas.cerrar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class CerrarEmpresaUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final EmpresasValidador validador;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void cerrar(CerrarEmpresaParametros parametros) {
        validador.empresaNoCerrada(parametros.getEmpresaId());
        validador.empresaNoCotizada(parametros.getEmpresaId());
        validador.directorEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());

        Empresa empresa = this.empresasService.getById(parametros.getEmpresaId());

        despedirATodosLosEmpleados(parametros.getEmpresaId());
        empresasService.save(empresa.cerrar());
        accionistasEmpresasService.deleteByEmpresaId(parametros.getEmpresaId());
        transaccionesService.save(Transaccion.builder()
                        .pagadoId(parametros.getJugadorId())
                        .pagadorId(parametros.getEmpresaId())
                        .pixelcoins(transaccionesService.getBalancePixelcions(parametros.getEmpresaId()))
                        .tipo(TipoTransaccion.EMPRESAS_CERRAR)
                .build());

        eventBus.publish(new EmpresaCerrada(empresa.getEmpresaId()));
    }

    private void despedirATodosLosEmpleados(UUID empresaId) {
        this.empleadosService.findEmpleoActivoByEmpresaId(empresaId).forEach(empleado -> {
            this.empleadosService.save(empleado.despedir("La empresa se ha cerrado"));
        });
    }
}
