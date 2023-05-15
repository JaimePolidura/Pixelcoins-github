package es.serversurvival.v1.empresas.accionistasserver.cancelarofertaccionserver;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.v1.empresas.ofertasaccionesserver.cancelarofertaccionserver.CancelarOfertaAccionServerUseCase;
import es.serversurvival.v1.empresas.ofertasaccionesserver.OfertasAccionesServerTestMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static es.serversurvival.v1.empresas.ofertasaccionesserver.OfertasAccionesServerTestMother.createOfertAccionServerJugador;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class CancelarOfertaAccionServerUseCaseTest {
    @Mock private OfertasAccionesServerService ofertasAccionesServerService;
    private CancelarOfertaAccionServerUseCase useCase;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new CancelarOfertaAccionServerUseCase(
                this.ofertasAccionesServerService
        );
    }

    @Test
    public void shouldCancelarOferta(){
        UUID idOferta = UUID.randomUUID();
        when(this.ofertasAccionesServerService.getById(idOferta)).thenReturn(OfertasAccionesServerTestMother.createOfertAccionServerJugador("jaime", "empresa"));

        this.useCase.cancelar("jaime", idOferta);

        verify(this.ofertasAccionesServerService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    public void ensureOwner(){
        UUID idOferta = UUID.randomUUID();
        when(this.ofertasAccionesServerService.getById(idOferta)).thenReturn(OfertasAccionesServerTestMother.createOfertAccionServerJugador("otro", "empresa"));

        assertThatCode(() -> this.useCase.cancelar("jaime", idOferta)).isInstanceOf(NotTheOwner.class);
    }

    @Test
    public void exists(){
        UUID idOferta = UUID.randomUUID();
        when(this.ofertasAccionesServerService.getById(idOferta)).thenThrow(ResourceNotFound.class);

        assertThatCode(() -> this.useCase.cancelar("lkajs", idOferta))
                .isInstanceOf(ResourceNotFound.class);
    }
}
