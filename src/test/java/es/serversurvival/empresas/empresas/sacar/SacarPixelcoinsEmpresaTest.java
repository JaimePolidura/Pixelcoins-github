package es.serversurvival.empresas.empresas.sacar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
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
public final class SacarPixelcoinsEmpresaTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EventBus eventBus;
    private SacarPixelcoinsUseCase useCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.useCase = new SacarPixelcoinsUseCase(this.empresasService, this.jugadoresService, this.eventBus);
    }

    @Test
    public void shouldSacar(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "jaime"));

        this.useCase.sacar("jaime", "empresa", 5);

        verify(this.empresasService, times(1)).save(Mockito.any(Empresa.class));
        verify(this.jugadoresService, times(1)).save(Mockito.any(Jugador.class));
        verify(this.eventBus, times(1)).publish(Mockito.any(PixelcoinsSacadasEvento.class));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));

        assertThatCode(() -> this.useCase.sacar("jaime", "empresa", 5))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));
        when(this.empresasService.getByNombre("empresaFail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.sacar("jaime", "empresaFail", 5))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void correctFormatPixelcoins(){
        assertThatCode(() -> this.useCase.sacar("", "", -1)).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.sacar("", "", 0)).isInstanceOf(IllegalQuantity.class);
    }

}
