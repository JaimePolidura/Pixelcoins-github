package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.bolsa.PosicionesAbiertasTestMother;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.bolsa.PosicionesAbiertasTestMother.createPosicionAbierta;
import static es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class AbrirOrdenUseCaseTest {
    @Mock private OrdenesPremarketService ordenesPremarketService;
    @Mock private PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    @Mock private EventBus eventBus;
    private AbrirOrdenUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new AbrirOrdenUseCase(
                this.ordenesPremarketService, this.posicionesAbiertasSerivce, this.eventBus
        );
    }

    @Test
    public void shouldCreateOrder(){
        UUID posicionAbiertaId = UUID.randomUUID();
        when(this.posicionesAbiertasSerivce.getById(posicionAbiertaId)).thenReturn(createPosicionAbierta("jaime", "AMZN"));
        when(this.ordenesPremarketService.isOrdenRegisteredFromPosicionAbierta("jaime", posicionAbiertaId))
                .thenReturn(false);

        this.useCase.abrirOrden(AbrirOrdenPremarketCommand.of("jaime", "AMZN", 1, LARGO_COMPRA, posicionAbiertaId));
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(this.ordenesPremarketService, times(1)).save(
                argThat(of("jaime")), argThat(of("AMZN")), argumentCaptor.capture(), argThat(of(LARGO_COMPRA)), argThat(of(posicionAbiertaId))
        );
        assertThat(argumentCaptor.getValue()).isEqualTo(1);

        verify(this.eventBus, times(1)).publish(argThat(of(
                OrdenAbiertaEvento.of("jaime", "AMZN", 1, LARGO_COMPRA, posicionAbiertaId)
        )));
    }

    @Test
    public void ordenAlreadyRegisteredForPosicionAbierta(){
        UUID posicionAbiertaId = UUID.randomUUID();
        when(this.posicionesAbiertasSerivce.getById(posicionAbiertaId)).thenReturn(createPosicionAbierta("jaime", "AMZN"));
        when(this.ordenesPremarketService.isOrdenRegisteredFromPosicionAbierta("jaime", posicionAbiertaId))
                .thenReturn(true);

        assertThatCode(() -> this.useCase.abrirOrden(AbrirOrdenPremarketCommand.of("jaime", "AMZN",
                1, LARGO_COMPRA, posicionAbiertaId)))
                .isInstanceOf(AlreadyExists.class);
    }

    @Test
    public void ownerOfPosicionAbierta(){
        UUID posicionAbiertaId = UUID.randomUUID();
        when(this.posicionesAbiertasSerivce.getById(posicionAbiertaId)).thenReturn(createPosicionAbierta("otro", "AMZN"));
        assertThatCode(() -> this.useCase.abrirOrden(AbrirOrdenPremarketCommand.of("jaime", "AMZN",
                1, LARGO_COMPRA, posicionAbiertaId)))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void posicionAbiertaExists(){
        UUID posicionAbiertaId = UUID.randomUUID();
        when(this.posicionesAbiertasSerivce.getById(posicionAbiertaId)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.abrirOrden(AbrirOrdenPremarketCommand.of("jaime", "AMZN",
                1, LARGO_COMPRA, posicionAbiertaId)))
                .isInstanceOf(ResourceNotFound.class);
    }
}
