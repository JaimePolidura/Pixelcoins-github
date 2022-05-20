package es.serversurvival.empresas.accionistasserver._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.MockitoArgEqualsMatcher;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistasServerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.of;
import static es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista.EMPRESA;
import static es.serversurvival.empresas.accionistasserver.AccionistasServerTestMother.createAccionnistaTipoJugaodor;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class AccionistasServerServiceTest {
    @Mock private AccionistasServerRepository repositoryDb;
    private AccionistasServerService service;

    @BeforeEach
    public void init(){
        this.service = new AccionistasServerService(
                this.repositoryDb
        );
    }

    @Test
    public void deleteById(){
        UUID idToDelete = UUID.randomUUID();
        this.service.deleteById(idToDelete);

        verify(this.repositoryDb, times(1)).deleteById(idToDelete);
    }

    @Test
    public void findByEmpresa(){
        when(this.repositoryDb.findByEmpresa("empresa")).thenReturn(List.of(
                createAccionnistaTipoJugaodor("jaime", "empresa"),
                createAccionnistaTipoJugaodor("paco", "empresa")
        ));

        assertThat(this.service.findByEmpresa("empresa"))
                .hasSize(2)
                .allMatch(e -> e.getEmpresa().equalsIgnoreCase("empresa"));
    }

    @Test
    public void getById(){
        UUID idToSearch1 = UUID.randomUUID();
        when(this.repositoryDb.findById(idToSearch1)).thenReturn(Optional.empty());
        assertThatCode(() -> this.service.getById(idToSearch1)).isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void save1(){
        AccionistaServer accionistaServerToSave = createAccionnistaTipoJugaodor("jaime", "empresa");
        this.service.save(accionistaServerToSave);
        verify(this.repositoryDb, times(1)).save(argThat(of(accionistaServerToSave)));
    }

    @Test
    public void save2(){
        this.service.save("jaime", EMPRESA, "empresa", 1, 1);
        ArgumentCaptor<AccionistaServer> accionistaServerArgumentCaptor = ArgumentCaptor.forClass(AccionistaServer.class);
        verify(this.repositoryDb, times(1)).save(accionistaServerArgumentCaptor.capture());

        var matches = accionistaServerArgumentCaptor.getAllValues().stream()
                .anyMatch(a -> a.esEmpresa() && a.getNombreAccionista().equalsIgnoreCase("jaime") && a.getCantidad() == 1 && a.getPrecioApertura() == 1);

        assertThat(matches).isTrue();
    }
}
