package es.serversurvival.bolsa.posicionesabiertas._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionesAbiertasRepository;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.bolsa.posicionesabiertas.PosicionesAbiertasTestMother.createPosicionAbierta;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class PosicionesAbiertasServiceTest {
    @Mock private PosicionesAbiertasRepository repositoryDb;
    private PosicionesAbiertasSerivce service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new PosicionesAbiertasSerivce(
                this.repositoryDb,
                new LRUCache<>(200)
        );
    }

    @Test
    public void deleleById(){
        var idToDelete = UUID.randomUUID();
        this.service.deleteById(idToDelete);
        verify(this.repositoryDb, times(1)).deleteById(idToDelete);
    }

    @Test
    public void existsByNombreActivo(){
        when(this.repositoryDb.findByNombreActivo("AMZN")).thenReturn(List.of(
                createPosicionAbierta("jaime", "AMZN"),
                createPosicionAbierta("jaime", "AMZN")
        ));
        assertThat(this.service.existsByNombreActivo("AMZN")).isTrue();
    }

    @Test
    public void findByNombreActivo(){
        when(this.repositoryDb.findByNombreActivo("AMZN")).thenReturn(List.of(
                createPosicionAbierta("jaime", "AMZN"),
                createPosicionAbierta("jaime", "AMZN")
        ));
        assertThat(this.service.findByNombreActivo("AMZN"))
                .hasSize(2);
    }

    @Test
    public void findAll(){
        when(this.repositoryDb.findAll()).thenReturn(List.of(
                createPosicionAbierta("jaime", "AMZN"),
                createPosicionAbierta("jaime", "GOOG")
        ));
        assertThat(this.service.findAll())
                .hasSize(2);
    }

    @Test
    public void findByJugador(){
        when(this.repositoryDb.findByJugador("jaime")).thenReturn(List.of(
                        createPosicionAbierta("jaime", "AMZN"),
                        createPosicionAbierta("jaime", "GOOG")
        ));
        assertThat(this.service.findByJugador("jaime"))
                .hasSize(2);
    }

    @Test
    public void getById(){
        UUID idFail = UUID.randomUUID();
        when(this.repositoryDb.findById(idFail)).thenReturn(Optional.empty());
        assertThatCode(() -> this.service.getById(idFail))
                .isInstanceOf(ResourceNotFound.class);

        var posicion = createPosicionAbierta("jaime", "MA");
        UUID idNotFail = posicion.getPosicionAbiertaId();
        when(this.repositoryDb.findById(idNotFail)).thenReturn(Optional.of(posicion));

        assertThat(this.service.getById(idNotFail)).isNotNull();
        assertThat(this.service.getById(idNotFail)).isNotNull();

        verify(this.repositoryDb, times(1)).findById(idNotFail);
    }

    @Test
    public void save2(){
        PosicionAbierta posicion = createPosicionAbierta("jaime", "AMZN");
        UUID idPosicion = this.service.save(posicion.getJugador(), posicion.getTipoActivo(), posicion.getNombreActivo(),
                posicion.getCantidad(), posicion.getPrecioApertura(), posicion.getTipoPosicion());

        verify(this.repositoryDb, times(1)).save(
                argThat(of(new PosicionAbierta(
                        idPosicion,
                        posicion.getJugador(),
                        posicion.getTipoActivo(),
                        posicion.getNombreActivo(),
                        posicion.getCantidad(),
                        posicion.getPrecioApertura(),
                        Funciones.hoy(),
                        posicion.getTipoPosicion()
                ))));
    }

    @Test
    public void save1(){
        PosicionAbierta posicionAbierta = createPosicionAbierta("jaime", "AMZN");
        this.service.save(posicionAbierta);
        verify(this.repositoryDb, times(1)).save(posicionAbierta);
    }
}
