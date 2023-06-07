package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class PagadorSueldosEmpresa {
    private final TransaccionesService transaccionesService;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void pagar(Empresa empresa) {
        empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId()).stream()
                .filter(Empleado::isEstaContratado)
                .forEach(empleado -> pagarSueldosPendientes(empresa, empleado));
    }

    private void pagarSueldosPendientes(Empresa empresa, Empleado empleado) {
        int numeroPagosPendientes = getNumeroSueldosPendientes(empleado);

        for (int i = 0; i < numeroPagosPendientes; i++) {
            double pixelcoinsEmpresa = transaccionesService.getBalancePixelcions(empresa.getEmpresaId());

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
        long ultimoPagoMs = toMillis(empleado.getFechaUltimoPago());
        long periodoPagosMs = empleado.getPeriodoPagoMs();
        long tiempoAhoraMs = System.currentTimeMillis();

        long tiempoTranscurridoDesdeUltimaPaga = tiempoAhoraMs - ultimoPagoMs;

        return (int) (tiempoTranscurridoDesdeUltimaPaga / periodoPagosMs);
    }
}
