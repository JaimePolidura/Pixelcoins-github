package es.serversurvival.v1.empresas.ofertasaccionesserver._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain.OfertasAccionesServerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.v1.MockitoArgEqualsMatcher.of;
import static es.serversurvival.v1.empresas.ofertasaccionesserver.OfertasAccionesServerTestMother.createOfertAccionServerJugador;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class OfertasAccionesServerServiceTest {
    @Mock private OfertasAccionesServerRepository repositoryDb;
    private OfertasAccionesServerService service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new OfertasAccionesServerService(this.repositoryDb);
    }

    @Test
    public void deleteById(){
        UUID idToDelete = UUID.randomUUID();
        this.service.deleteById(idToDelete);
        verify(this.repositoryDb, times(1)).deleteById(idToDelete);
    }

    @Test
    public void findByOfertanteNombre(){
        when(this.repositoryDb.findByOfertanteNombre("jaime")).thenReturn(List.of(
                createOfertAccionServerJugador("jaime", "empresa"),
                createOfertAccionServerJugador("jaime", "empresa")
        ));
        Assertions.assertThat(this.service.findByOfertanteNombre("jaime")).hasSize(2);
    }

    @Test
    public void findByEmpresa(){
        when(this.repositoryDb.findByEmpresa("empresa")).thenReturn(List.of(
                createOfertAccionServerJugador("putin", "empresa"),
                createOfertAccionServerJugador("as", "empresa")
        ));
        Assertions.assertThat(this.service.findByEmpresa("empresa")).hasSize(2);
    }

    @Test
    public void findAll(){
        when(this.repositoryDb.findAll()).thenReturn(List.of(
                createOfertAccionServerJugador("putin", "jaime"),
                createOfertAccionServerJugador("as", "lsusci1a")
        ));
        Assertions.assertThat(this.service.findAll()).hasSize(2);
    }

    @Test
    public void getById(){
        UUID idToSearch = UUID.randomUUID();
        when(this.repositoryDb.findById(idToSearch)).thenReturn(Optional.empty());
        assertThatCode(() -> this.service.getById(idToSearch)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void save1(){
        OfertaAccionServer oferta = createOfertAccionServerJugador("jaime", "empresa");
        this.service.save(oferta);
        verify(this.repositoryDb, times(1)).save(argThat(of(oferta)));
    }

    @Test
    public void save2(){
        this.service.save("jaime", "empresa", 10, 1, TipoAccionista.JUGADOR, 1, UUID.randomUUID());
        var ofertaAccionServerArgumentCaptor = ArgumentCaptor.forClass(OfertaAccionServer.class);
        verify(this.repositoryDb, times(1)).save(ofertaAccionServerArgumentCaptor.capture());
        var matches = ofertaAccionServerArgumentCaptor.getAllValues().stream()
                .anyMatch(a -> a.getEmpresa().equalsIgnoreCase("empresa") &&
                        a.getNombreOfertante().equalsIgnoreCase("jaime")
                        && a.getCantidad() == 1 && a.getPrecioApertura() == 1
                        && a.getTipoOfertante() == TipoAccionista.JUGADOR && a.getPrecio() == 10);

        assertThat(matches).isTrue();
    }
}
