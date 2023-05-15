package es.serversurvival.v1.empresas.empresas.pagardividendos;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoEmpresa;
import es.serversurvival.v1.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.empresas.accionistasserver.AccionistasServerTestMother;
import es.serversurvival.v1.jugadores.JugadoresTestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.*;
import static es.serversurvival.v1.empresas.accionistasserver.AccionistasServerTestMother.createAccionistaTipoEmpresa;
import static es.serversurvival.v1.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.v1.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.v1.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarDividendosEmpresaServerUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private AccionistasServerService accionistasEmpresasServerService;
    @Mock private EventBus eventBus;
    private PagarDividendosEmpresaServerUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new PagarDividendosEmpresaServerUseCase(
                this.empresasService,
                this.jugadoresService,
                this.accionistasEmpresasServerService,
                this.eventBus
        );
    }

    @Test
    public void shouldMakeDividendPayment(){
        final int accionesEmpresa = 6;
        final int accionesJugador1 = 4;
        Empresa empresaToPayDividend = createEmpresa("empresa", "jaime", 1000).setCotizadaToTrue().withAccionesTotales(10);
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToPayDividend);
        AccionistaServer accionista1 = AccionistasServerTestMother.createAccionnistaTipoJugaodor("pedro", "empresa", accionesJugador1);
        Jugador jugadorAccionista1 = JugadoresTestMother.createJugador("pedro");
        AccionistaServer accionista3 = AccionistasServerTestMother.createAccionistaTipoEmpresa("empresa", "empresa", accionesEmpresa);
        List<AccionistaServer> accionistas = List.of(accionista1, accionista3);
        when(this.accionistasEmpresasServerService.findByEmpresa("empresa")).thenReturn(accionistas);
        when(this.jugadoresService.getByNombre("pedro")).thenReturn(jugadorAccionista1);

        this.useCase.pagar("jaime", "empresa", 10);

        verify(this.empresasService, times(1)).save(argThat(of(
                empresaToPayDividend.incrementPixelcoinsBy(10 * accionesEmpresa)
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpresaServerDividendoPagadoEmpresa.of("empresa", 10 * accionesEmpresa)
        )));

        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorAccionista1.incrementPixelcoinsBy(accionesJugador1 * 10).incrementIngresosBy(accionesJugador1 * 10)
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                EmpresaServerDividendoPagadoJugador.of(jugadorAccionista1.getNombre(), "empresa", 10 * accionesJugador1)
        )));


        verify(this.empresasService, times(1)).save(argThat(of(
                empresaToPayDividend.decrementPixelcoinsBy(10 * 10)
        )));
    }

    @Test
    public void dividendoCorrectFormat(){
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", -1)).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 0)).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 1)).isNotInstanceOf(IllegalQuantity.class);
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 10)).
                isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresa")).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 10))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void empresaCotizada(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 10))
                .isInstanceOf(IllegalState.class);

    }

    @Test
    public void enoughPixelcions(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime", 5)
                .setCotizadaToTrue()
                .withAccionesTotales(10));
        assertThatCode(() -> this.useCase.pagar("jaime", "empresa", 10))
                .isInstanceOf(NotEnoughPixelcoins.class);

    }
}
