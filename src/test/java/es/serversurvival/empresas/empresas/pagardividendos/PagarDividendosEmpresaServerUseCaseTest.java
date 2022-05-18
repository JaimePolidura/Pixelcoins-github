package es.serversurvival.empresas.empresas.pagardividendos;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoEmpresa;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionistaTipoEmpresa;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PagarDividendosEmpresaServerUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private AccionistasEmpresasServerService accionistasEmpresasServerService;
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
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));
        when(this.jugadoresService.getByNombre("pedro")).thenReturn(createJugador("pedro"));
        when(this.accionistasEmpresasServerService.findByEmpresa("empresa")).thenReturn(List.of(
                createAccionnistaTipoJugaodor("pedro", "empresa"),
                createAccionistaTipoEmpresa("empresa", "empresa"),
                createAccionistaTipoEmpresa("empresa", "empresa")
        ));

        this.useCase.pagar("jaime", "empresa", 10);

        verify(this.jugadoresService, times(1)).save(Mockito.any(Jugador.class));
        verify(this.eventBus, times(1)).publish(Mockito.any(EmpresaServerDividendoPagadoJugador.class));

        verify(this.empresasService, times(3)).save(Mockito.any(Empresa.class));
        verify(this.eventBus, times(2)).publish(Mockito.any(EmpresaServerDividendoPagadoEmpresa.class));
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
}
