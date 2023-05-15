package es.serversurvival.v1.deudas.cancelar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.deudas._shared.domain.Deuda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.deudas.DeudasTestMother.createDeuda;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CancelarDeudaUseCaseTest {
    @Mock private DeudasService deudasService;
    @Mock private EventBus eventBus;
    private CancelarDeudaUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new CancelarDeudaUseCase(
                this.deudasService,
                this.eventBus
        );
    }

    @Test
    public void shouldCancel(){
        Deuda deudaToCancel = createDeuda("jaime", "otro");
        when(this.deudasService.getById(deudaToCancel.getDeudaId())).thenReturn(deudaToCancel);
        this.useCase.cancelarDeuda("jaime", deudaToCancel.getDeudaId());

        verify(this.deudasService, times(1)).deleteById(deudaToCancel.getDeudaId());
        verify(this.eventBus, times(1)).publish(argThat(of(
                DeudaCanceladaEvento.of("jaime", "otro", 10)
        )));
    }

    @Test
    public void notAcredor(){
        UUID deudaId = UUID.randomUUID();
        when(this.deudasService.getById(deudaId)).thenReturn(createDeuda("otro", "jaime"));
        assertThatCode(() -> this.useCase.cancelarDeuda("lkjas", deudaId))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void deudaExists(){
        UUID deudaIdFail = UUID.randomUUID();
        when(this.deudasService.getById(deudaIdFail)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.cancelarDeuda("lkjas", deudaIdFail))
                .isInstanceOf(ResourceNotFound.class);
    }
}
