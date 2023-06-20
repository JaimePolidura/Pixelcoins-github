package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival._shared.TiempoService;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagadorSueldosEmpresaUseCase implements UseCaseHandler<PagadorSueldosEmpresaParametros> {
    private final TransaccionesService transaccionesService;
    private final EmpleadosService empleadosService;
    private final TiempoService tiempoService;
    private final EventBus eventBus;

    @Override
    public void handle(PagadorSueldosEmpresaParametros parametros) {
        empleadosService.findEmpleoActivoByEmpresaId(parametros.getEmpresa().getEmpresaId())
                .forEach(empleado -> pagarSueldosPendientes(parametros.getEmpresa(), empleado));
    }

    private void pagarSueldosPendientes(Empresa empresa, Empleado empleado) {
        int numeroPagosPendientes = getNumeroSueldosPendientes(empleado);

        for (int i = 0; i < numeroPagosPendientes; i++) {
            double pixelcoinsEmpresa = transaccionesService.getBalancePixelcoins(empresa.getEmpresaId());

            if(empleado.getSueldo() > pixelcoinsEmpresa){
                eventBus.publish(new ErrorPagandoSueldoEmpresa(empleado.getEmpleadoId(), empleado.getSueldo()));
                break;
            }

            pagarSueldo(empresa, empleado);
        }
    }

    private void pagarSueldo(Empresa empresa, Empleado empleado) {
        empleadosService.save(empleado.marcarSueldoPagado());
        transaccionesService.save(Transaccion.builder()
                .pagadorId(empresa.getEmpresaId())
                .pagadoId(empleado.getEmpleadoId())
                .pixelcoins(empleado.getSueldo())
                .tipo(TipoTransaccion.EMPRESAS_SUELDO)
                .build());
    }

    private int getNumeroSueldosPendientes(Empleado empleado) {
        long ultimoPagoMs = tiempoService.toMillis(empleado.getFechaUltimoPago());
        long periodoPagosMs = empleado.getPeriodoPagoMs();
        long tiempoAhoraMs = tiempoService.millis();

        long tiempoTranscurridoDesdeUltimaPaga = tiempoAhoraMs - ultimoPagoMs;

        return (int) (tiempoTranscurridoDesdeUltimaPaga / periodoPagosMs);
    }
}
