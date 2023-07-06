package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.jaime.EventBus;
import es.serversurvival.ArgPredicateMatcher;
import es.serversurvival._shared.TiempoService;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.domain.EstadoDeuda;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public final class PagarDeudaCuotasUseCaseTest {
    private PagarDeudaCuotasUseCase useCase;
    private MovimientosService movimientosService;
    private TransaccionesSaver transaccionesSaver;
    private OfertasService ofertasService;
    private DeudasService deudasService;
    private TiempoService tiempoActual;
    private EventBus eventBus;

    @BeforeEach
    public void init() {
        this.movimientosService = mock(MovimientosService.class);
        this.transaccionesSaver = mock(TransaccionesSaver.class);
        this.ofertasService = mock(OfertasService.class);
        this.deudasService = mock(DeudasService.class);
        this.tiempoActual = mock(TiempoService.class);
        this.eventBus = mock(EventBus.class);

        this.useCase = new PagarDeudaCuotasUseCase(movimientosService, transaccionesSaver, ofertasService, deudasService, tiempoActual, eventBus);
    }

    @Test
    public void dosCutoasPendientes_2SePagan_1CuotaNomrla_2Reembolso() {
        long periodoPagoCuota = 1000L;
        long ultimoPagoToMillis = 1000L;
        long tiempoActualMillis = 3500;
        double interes = 0.1;
        double nominal = 100;
        double cuota = interes * nominal;
        UUID acredor = UUID.randomUUID();
        UUID deudor = UUID.randomUUID();
        LocalDateTime ultimoPagoDeuda = LocalDateTime.now();

        when(movimientosService.getBalance(deudor)).thenReturn(10000.0D);
        when(tiempoActual.toMillis(any())).thenReturn(ultimoPagoToMillis);
        when(tiempoActual.millis()).thenReturn(tiempoActualMillis);

        Deuda deuda = Deuda.builder()
                .periodoPagoCuotaMs(periodoPagoCuota)
                .acredorJugadorId(acredor)
                .deudorJugadorId(deudor)
                .nCuotasTotales(4)
                .fechaUltimoPagoCuota(ultimoPagoDeuda)
                .nCuotasPagadas(2)
                .deudaId(deudor)
                .interes(interes)
                .nominal(nominal)
                .build();
        this.useCase.handle(PagarDeudaCuotasParametros.from(deuda));

        verify(deudasService, times(2)).save(any(Deuda.class));
        assertThat(deuda.getNCuotasPagadas()).isEqualTo(4); //Se modifica por referencia
        assertThat(deuda.getEstadoDeuda()).isEqualTo(EstadoDeuda.PAGADA); //Se modifica por referencia
        assertThat(deuda.getFechaUltimoPagoCuota()).isEqualTo(ultimoPagoDeuda.plusNanos(periodoPagoCuota * 1_000_000 * 2));

        verify(transaccionesSaver, times(2)).save(any(Transaccion.class));
        verify(transaccionesSaver, times(1)).save(argThat(ArgPredicateMatcher.of(transaccion ->
                transaccion.getPagadorId().equals(deudor) &&
                        transaccion.getPagadoId().equals(acredor) &&
                        transaccion.getPixelcoins() == cuota &&
                        transaccion.getTipo() == TipoTransaccion.DEUDAS_CUOTA &&
                        transaccion.getObjeto().equals(deuda.getDeudaId().toString()))));
        verify(transaccionesSaver, times(1)).save(argThat(ArgPredicateMatcher.of(transaccion ->
                transaccion.getPagadorId().equals(deudor) &&
                        transaccion.getPagadoId().equals(acredor) &&
                        transaccion.getPixelcoins() == cuota + nominal &&
                        transaccion.getTipo() == TipoTransaccion.DEUDAS_CUOTA &&
                        transaccion.getObjeto().equals(deuda.getDeudaId().toString()))));

        verify(ofertasService, times(1)).deleteByObjetoYTipo(eq(deuda.getDeudaId().toString()), eq(TipoOferta.DEUDA_MERCADO_SECUNDARIO));
    }

    @Test
    public void dosCutoasPendientes_2SePagan() {
        long periodoPagoCuota = 1000L;
        long ultimoPagoToMillis = 1000L;
        long tiempoActualMillis = 3500;
        double interes = 0.1;
        double nominal = 100;
        double cuota = interes * nominal;
        UUID acredor = UUID.randomUUID();
        UUID deudor = UUID.randomUUID();
        LocalDateTime ultimoPagoDeuda = LocalDateTime.now();

        when(movimientosService.getBalance(deudor)).thenReturn(10000.0D);
        when(tiempoActual.toMillis(any())).thenReturn(ultimoPagoToMillis);
        when(tiempoActual.millis()).thenReturn(tiempoActualMillis);

        Deuda deuda = Deuda.builder()
                .periodoPagoCuotaMs(periodoPagoCuota)
                .acredorJugadorId(acredor)
                .deudorJugadorId(deudor)
                .nCuotasTotales(4)
                .fechaUltimoPagoCuota(ultimoPagoDeuda)
                .nCuotasPagadas(1)
                .deudaId(deudor)
                .interes(interes)
                .nominal(nominal)
                .build();
        this.useCase.handle(PagarDeudaCuotasParametros.from(deuda));

        verify(deudasService, times(2)).save(any(Deuda.class));
        assertThat(deuda.getNCuotasPagadas()).isEqualTo(3); //Se modifica por referencia
        assertThat(deuda.getFechaUltimoPagoCuota()).isEqualTo(ultimoPagoDeuda.plusNanos(periodoPagoCuota * 1_000_000 * 2));

        verify(transaccionesSaver, times(2)).save(any(Transaccion.class));
        verify(transaccionesSaver, times(2)).save(argThat(ArgPredicateMatcher.of(transaccion ->
                transaccion.getPagadorId().equals(deudor) &&
                        transaccion.getPagadoId().equals(acredor) &&
                        transaccion.getPixelcoins() == cuota &&
                        transaccion.getTipo() == TipoTransaccion.DEUDAS_CUOTA &&
                        transaccion.getObjeto().equals(deuda.getDeudaId().toString()))));

        verify(ofertasService, never()).deleteByObjetoYTipo(any(), any());
    }

    @Test
    public void noSePuedePagar() {
        long periodoPagoCuota = 1000L;
        long ultimoPagoToMillis = 1000L;
        long tiempoActualMillis = 2000L;
        double interes = 0.1;
        double nominal = 100;
        UUID acredor = UUID.randomUUID();
        UUID deudor = UUID.randomUUID();
        LocalDateTime ultimoPagoDeuda = LocalDateTime.now();

        when(movimientosService.getBalance(deudor)).thenReturn(9.0d);
        when(tiempoActual.toMillis(any())).thenReturn(ultimoPagoToMillis);
        when(tiempoActual.millis()).thenReturn(tiempoActualMillis);
        Deuda deuda = Deuda.builder()
                .periodoPagoCuotaMs(periodoPagoCuota)
                .acredorJugadorId(acredor)
                .deudorJugadorId(deudor)
                .nCuotasTotales(4)
                .fechaUltimoPagoCuota(ultimoPagoDeuda)
                .nCuotasPagadas(1)
                .nCuotasImpagadas(1)
                .deudaId(deudor)
                .interes(interes)
                .nominal(nominal)
                .build();
        this.useCase.handle(PagarDeudaCuotasParametros.from(deuda));

        verify(transaccionesSaver, never()).save(any());

        verify(this.deudasService, times(1)).save(any());
        assertThat(deuda.getNCuotasImpagadas()).isEqualTo(2);
    }

    @Test
    public void noHayCutoasPendientes() {
        long periodoPagoCuota = 1000L;
        long ultimoPagoToMillis = 2000L;
        long tiempoActualMillis = 2500L;

        when(tiempoActual.toMillis(any())).thenReturn(ultimoPagoToMillis);
        when(tiempoActual.millis()).thenReturn(tiempoActualMillis);

        this.useCase.handle(PagarDeudaCuotasParametros.from(Deuda.builder()
                .periodoPagoCuotaMs(periodoPagoCuota)
                .fechaUltimoPagoCuota(LocalDateTime.now())
                .build()));

        verify(transaccionesSaver, never()).save(any());
    }
}
