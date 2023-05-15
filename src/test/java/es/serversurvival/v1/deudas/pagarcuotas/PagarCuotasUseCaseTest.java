package es.serversurvival.v1.deudas.pagarcuotas;

import es.jaime.EventBus;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.deudas._shared.domain.Deuda;
import es.serversurvival.v1.deudas.pagarCuotas.DeudaCuotaNoPagadaEvento;
import es.serversurvival.v1.deudas.pagarCuotas.DeudaCuotaPagadaEvento;
import es.serversurvival.v1.deudas.pagarCuotas.PagarDeudasCuotasUseCase;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.*;
import static es.serversurvival.v1.deudas.DeudasTestMother.createDeuda;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarCuotasUseCaseTest {
    @Mock private DeudasService deudasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EventBus eventBus;
    private PagarDeudasCuotasUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new PagarDeudasCuotasUseCase(
                this.deudasService,
                this.jugadoresService,
                this.eventBus
        );
    }

    @Test
    public void shouldPay(){
        Jugador jaime = JugadoresTestMother.createJugador("jaime", 0);
        Jugador pedro = JugadoresTestMother.createJugador("pedro", 1000);
        Jugador acredor = JugadoresTestMother.createJugador("acredor", 1000);
        Jugador deudor = JugadoresTestMother.createJugador("deudor", 1000);
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jaime);
        when(this.jugadoresService.getByNombre("pedro")).thenReturn(pedro);
        when(this.jugadoresService.getByNombre("acredor")).thenReturn(acredor);
        when(this.jugadoresService.getByNombre("deudor")).thenReturn(deudor);
        Deuda deudaFueraDias = createDeuda("jaime", "pedro");
        Deuda deudaAPagar = createDeuda("jaime", "pedro", "1999-02-02");
        Deuda deudaDeudorSinPixelcoins = createDeuda("pedro", "jaime", "1999-02-02");
        Deuda deudaUltimoDIa = createDeuda("acredor", "deudor", "1999-02-02", 1);
        List<Deuda> allDeudas = List.of(deudaFueraDias, deudaAPagar, deudaDeudorSinPixelcoins, deudaUltimoDIa);
        when(this.deudasService.findAll()).thenReturn(allDeudas);

        this.useCase.pagarDeudas();

        //Ultima cuota a pagar -> se borra
        verify(this.deudasService, times(1)).deleteById(argThat(of(
                deudaUltimoDIa.getDeudaId()
        )));
        verify(this.jugadoresService, times(1)).realizarTransferencia(
                argThat(of(deudor)), argThat(of(acredor)), anyDouble()
        );
        verify(this.jugadoresService, times(1)).save(argThat(of(
                deudor.incrementNPagos()
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                DeudaCuotaPagadaEvento.of(deudaUltimoDIa.getDeudaId(), "acredor", "deudor",
                        deudaUltimoDIa.getCuota(), deudaUltimoDIa.getTiempoRestante() - 1)
        )));

        //Pedro deudor -> tiene pixelcoins para pagar la deuda, le quedan 5 dias
        verify(this.deudasService, times(1)).save(argThat(of(
                deudaAPagar.decrementPixelcoinsRestantes(deudaAPagar.getCuota()).decrementTiempoRestanteByOne()
                        .withFechaUltimoPago(Funciones.hoy())
        )));
        verify(this.jugadoresService, times(1)).realizarTransferencia(
                argThat(of(pedro)), argThat(of(jaime)), anyDouble()
        );
        verify(this.jugadoresService, times(1)).save(argThat(of(
                pedro.incrementNPagos()
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                DeudaCuotaPagadaEvento.of(deudaAPagar.getDeudaId(), "jaime", "pedro",
                        deudaAPagar.getCuota(), deudaAPagar.getTiempoRestante() - 1)
        )));

        //Jaime deudor -> no tiene pixelcoins para pagar la deuda
        verify(this.eventBus, times(1)).publish(argThat(of(
                DeudaCuotaNoPagadaEvento.of("pedro", "jaime", deudaDeudorSinPixelcoins.getDeudaId())
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jaime.incrementNInpago()
        )));
    }

}
