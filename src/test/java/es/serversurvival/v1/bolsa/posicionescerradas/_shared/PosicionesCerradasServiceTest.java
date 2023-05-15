package es.serversurvival.v1.bolsa.posicionescerradas._shared;

import es.serversurvival.v1.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.PosicionesCerradasRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static es.serversurvival.v1.bolsa.posicionescerradas.PosicionesCerradasTestMother.createPosicionCerrada;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PosicionesCerradasServiceTest {
    @Mock private PosicionesCerradasRepository repositoryDb;
    private PosicionesCerradasService service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new PosicionesCerradasService(this.repositoryDb);
    }

    @Test
    public void save(){
        var posicionCerrada = createPosicionCerrada("jaime", "GOOG");
        this.service.save(posicionCerrada);
        verify(this.repositoryDb, times(1)).save(posicionCerrada);
    }

    @Test
    public void findByJugador(){
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(
                createPosicionCerrada("jaime", "GOOG"),
                createPosicionCerrada("jaime", "MA")
        ));

        Assertions.assertThat(this.service.findByJugador("jaime")).hasSize(2);

        verify(this.repositoryDb, times(1)).findByJugador("jaime");
    }
}
