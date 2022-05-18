package es.serversurvival.empresas.empresas.comprarservicio;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.empresas.EmpresasTestMother;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores.JugadoresTestMother;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.jugadores.JugadoresTestMother.createJugador;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ComprarServicioUseCaseTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EventBus eventBus;
    private ComprarServicioUseCase useCase;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new ComprarServicioUseCase(this.empresasService, this.jugadoresService, this.eventBus);
    }

    @Test
    public void shouldComprarServicio(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "other"));
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 100));

        this.useCase.comprar("jaime", "empresa", 10);

        verify(this.empresasService, times(1)).save(Mockito.any(Empresa.class));
        verify(this.jugadoresService, times(1)).save(Mockito.any(Jugador.class));
        verify(this.eventBus, times(1)).publish(Mockito.any(EmpresaServicioCompradoEvento.class));
    }

    @Test
    public void ensureNotOwner(){
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));

        assertThatCode(() -> this.useCase.comprar("jaime", "empresa", 10)).isInstanceOf(CannotBeYourself.class);
    }

    @Test
    public void empresaExists(){
        when(this.empresasService.getByNombre("empresaFail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.comprar("jaime", "empresaFail", 10)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void incorrectFormatPixelcoins(){
        assertThatCode(() -> this.useCase.comprar("", "", -1)).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.comprar("", "", 0)).isInstanceOf(IllegalQuantity.class);
    }
}
