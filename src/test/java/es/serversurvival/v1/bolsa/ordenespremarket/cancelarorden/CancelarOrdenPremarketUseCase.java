package es.serversurvival.v1.bolsa.ordenespremarket.cancelarorden;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.v1.bolsa.ordenespremarket.cancelarorderpremarket.CancelarOrdenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.v1.bolsa.ordenespremarket.OrdernesPremarketTestMother.createOrdenPremarket;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CancelarOrdenPremarketUseCase {
    @Mock private OrdenesPremarketService ordenesPremarketService;
    private CancelarOrdenUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new CancelarOrdenUseCase(this.ordenesPremarketService);
    }

    @Test
    public void shouldCancelar(){
        var ordenToCancelar = createOrdenPremarket("jaime", "otro");
        when(this.ordenesPremarketService.getById(ordenToCancelar.getOrderPremarketId())).thenReturn(ordenToCancelar);

        this.useCase.cancelar("jaime", ordenToCancelar.getOrderPremarketId());

        verify(this.ordenesPremarketService, times(1)).deleteById(ordenToCancelar.getOrderPremarketId());
    }

    @Test
    public void notTheOwnerOfOrder(){
        UUID ordenId = UUID.randomUUID();
        when(this.ordenesPremarketService.getById(ordenId)).thenReturn(createOrdenPremarket("otro", "otro"));
        assertThatCode(() -> this.useCase.cancelar("jaime", ordenId))
                .isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void orderExists(){
        UUID ordenId = UUID.randomUUID();
        when(this.ordenesPremarketService.getById(ordenId)).thenThrow(ResourceNotFound.class);
        assertThatCode(() -> this.useCase.cancelar("jaime", ordenId))
                .isInstanceOf(ResourceNotFound.class);
    }
}
