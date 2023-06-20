package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.jaime.EventBus;
import es.serversurvival.ArgPredicateMatcher;
import es.serversurvival._shared.TiempoService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static es.serversurvival.pixelcoins.transacciones.TipoTransaccion.EMPRESAS_SUELDO;
import static org.mockito.Mockito.*;

public class PagadorSueldosEmpresaUseCaseTest {
    private PagadorSueldosEmpresaUseCase useCase;
    private TransaccionesService transaccionesService;
    private EmpleadosService empleadosService;
    private TiempoService tiempoService;
    private EventBus eventBus;

    @BeforeEach
    public void init() {
        this.transaccionesService = mock(TransaccionesService.class);
        this.empleadosService = mock(EmpleadosService.class);
        this.tiempoService = mock(TiempoService.class);
        this.eventBus = mock(EventBus.class);
        this.useCase = new PagadorSueldosEmpresaUseCase(transaccionesService, empleadosService, tiempoService, eventBus);
    }

    @Test
    public void noSeHacenPagos() {
        Empresa empresa = Empresa.builder().empresaId(UUID.randomUUID()).build();
        LocalDateTime ultimoPago = LocalDateTime.now();
        UUID empleado1 = UUID.randomUUID();

        when(transaccionesService.getBalancePixelcoins(empresa.getEmpresaId())).thenReturn(10.0d);
        when(empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId())).thenReturn(List.of(
                Empleado.builder().empleadoId(empleado1).periodoPagoMs(100L).sueldo(100).fechaUltimoPago(ultimoPago).build()
        ));
        when(tiempoService.millis()).thenReturn(1500L);
        when(tiempoService.toMillis(ultimoPago)).thenReturn(1000L);

        verify(transaccionesService, never()).save(any());
    }

    @Test
    public void tresEmpleados_2PagosPendientes() {
        Empresa empresa = Empresa.builder().empresaId(UUID.randomUUID()).build();
        LocalDateTime ultimoPago = LocalDateTime.now();
        UUID empleado1 = UUID.randomUUID(), empleado2 = UUID.randomUUID(), empleado3 = UUID.randomUUID();

        when(transaccionesService.getBalancePixelcoins(empresa.getEmpresaId())).thenReturn(100000.0d);
        when(empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId())).thenReturn(List.of(
                Empleado.builder().empleadoId(empleado1).periodoPagoMs(600L).sueldo(100).fechaUltimoPago(ultimoPago).build(), //No pagos
                Empleado.builder().empleadoId(empleado2).periodoPagoMs(400L).sueldo(200).fechaUltimoPago(ultimoPago).build(), //1 Pago
                Empleado.builder().empleadoId(empleado3).periodoPagoMs(200L).sueldo(300).fechaUltimoPago(ultimoPago).build() //2 pagos
        ));
        when(tiempoService.millis()).thenReturn(1500L);
        when(tiempoService.toMillis(ultimoPago)).thenReturn(1000L);

        useCase.handle(PagadorSueldosEmpresaParametros.from(empresa));

        verify(transaccionesService, times(3)).save(any());
        verify(empleadosService, times(3)).save(any());
        verify(empleadosService, never()).save(argThat(ArgPredicateMatcher.of(empleado ->
                empleado.getEmpleadoId().equals(empleado1))));

        //EMPLEADO 2
        verify(empleadosService, times(1)).save(argThat(ArgPredicateMatcher.of(empleado ->
                empleado.getEmpleadoId().equals(empleado2) &&
                empleado.getFechaUltimoPago().equals(ultimoPago.plusNanos(1_000_000 * 400L)))));
        verify(transaccionesService, times(1)).save(argThat(ArgPredicateMatcher.of(transaccion ->
                transaccion.getPagadorId().equals(empresa.getEmpresaId()) &&
                transaccion.getPagadoId().equals(empleado2) &&
                transaccion.getPixelcoins() == 200.0D &&
                transaccion.getTipo() == EMPRESAS_SUELDO)));

        //EMPLEADO 3
        verify(empleadosService, times(2)).save(argThat(ArgPredicateMatcher.of(empleado ->
                empleado.getEmpleadoId().equals(empleado3))));
        verify(empleadosService, atLeast(1)).save(argThat(ArgPredicateMatcher.of(empleado ->
                empleado.getEmpleadoId().equals(empleado3) &&
                empleado.getFechaUltimoPago().equals(ultimoPago.plusNanos(1_000_000 * 200L * 2)))));
        verify(transaccionesService, times(2)).save(argThat(ArgPredicateMatcher.of(transaccion ->
                transaccion.getPagadorId().equals(empresa.getEmpresaId()) &&
                transaccion.getPagadoId().equals(empleado3) &&
                transaccion.getPixelcoins() == 300.0D &&
                transaccion.getTipo() == EMPRESAS_SUELDO)));
    }

    @Test
    public void noHayPagosPendientes() {
        Empresa empresa = Empresa.builder().empresaId(UUID.randomUUID()).build();
        LocalDateTime ultimoPago = LocalDateTime.now();

        when(empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId())).thenReturn(List.of(
                Empleado.builder().periodoPagoMs(1000L).fechaUltimoPago(ultimoPago).build(),
                Empleado.builder().periodoPagoMs(1000L).fechaUltimoPago(ultimoPago).build()
        ));
        when(tiempoService.millis()).thenReturn(1500L);
        when(tiempoService.toMillis(ultimoPago)).thenReturn(900L);

        useCase.handle(PagadorSueldosEmpresaParametros.from(empresa));

        verify(transaccionesService, never()).save(any());
    }
}
