package es.serversurvival.empresas.empresas.depositar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
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

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static es.serversurvival.empresas.empresas.EmpresasTestMother.createEmpresa;
import static es.serversurvival.jugadores.JugadoresTestMother.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class DepositarPixelcoinsEmpresaTest {
    @Mock private EmpresasService empresasService;
    @Mock private JugadoresService jugadoresService;
    @Mock private EventBus eventBus;
    private DepositarPixelcoinsUseCase useCase;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.useCase = new DepositarPixelcoinsUseCase(this.jugadoresService, this.empresasService, this.eventBus);
    }

    @Test
    public void shouldMakeDeposit(){
        Empresa empresaToDeposit = createEmpresa("empresa", "jaime");
        Jugador jugadorDepositador = createJugador("jaime", 10);

        when(this.jugadoresService.getByNombre("jaime")).thenReturn(jugadorDepositador);
        when(this.empresasService.getByNombre("empresa")).thenReturn(empresaToDeposit);

        this.useCase.depositar("empresa", "jaime", 5);

        empresaToDeposit = empresaToDeposit.incrementPixelcoinsBy(5);
        jugadorDepositador = jugadorDepositador.decrementPixelcoinsBy(5);

        verify(this.empresasService, times(1)).save(argThat(of(
                empresaToDeposit
        )));
        verify(this.jugadoresService, times(1)).save(argThat(of(
                jugadorDepositador
        )));
        verify(this.eventBus, times(1)).publish(argThat(of(
                PixelcoinsDepositadasEvento.of(jugadorDepositador, empresaToDeposit, 5)
        )));
    }

    @Test
    public void ownerOfEmpresa(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));
        when(this.empresasService.getByNombre("empresa")).thenReturn(createEmpresa("empresa", "otro"));

        assertThatCode(() -> this.useCase.depositar("empresa", "jaime", 5))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void empresaExists(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));
        when(this.empresasService.getByNombre("empresaFail")).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.depositar("empresaFail", "jaime", 5))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void hasEnoughPixelcoins(){
        when(this.jugadoresService.getByNombre("jaime")).thenReturn(createJugador("jaime", 10));

        assertThatCode(() -> this.useCase.depositar("", "jaime", 20))
                .isInstanceOf(NotEnoughPixelcoins.class);
    }

    @Test
    public void incorrectFormatPixelcoins(){
        assertThatCode(() -> this.useCase.depositar("", "", -1)).isInstanceOf(IllegalQuantity.class);
        assertThatCode(() -> this.useCase.depositar("", "", 0)).isInstanceOf(IllegalQuantity.class);
    }

}
