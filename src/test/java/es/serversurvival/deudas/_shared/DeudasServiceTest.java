package es.serversurvival.deudas._shared;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.deudas._shared.domain.DeudasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.serversurvival.MockitoArgEqualsMatcher.*;
import static es.serversurvival.deudas.DeudasTestMother.createDeuda;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class DeudasServiceTest {
    @Mock private DeudasRepository deudasRepository;
    private DeudasService deudasService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.deudasService = new DeudasService(this.deudasRepository);
    }

    @Test
    public void getAllDeudasDeudorAndAcredorMap(){
        when(this.deudasRepository.findAll()).thenReturn(List.of(
                createDeuda("otro", "jaime"),
                createDeuda("jaime", "otro"),
                createDeuda("jaime", "pedro")
        ));
        var mapAcredor = this.deudasService.getAllDeudasAcredorMap();
        assertThat(mapAcredor).isNotEmpty().hasSize(2);
        assertThat(mapAcredor.get("otro")).isNotNull().isNotEmpty().hasSize(1);
        assertThat(mapAcredor.get("jaime")).isNotNull().isNotEmpty().hasSize(2);

        var mapDeudor = this.deudasService.getAllDeudasDeudorMap();
        assertThat(mapDeudor).isNotEmpty().hasSize(3);
        assertThat(mapDeudor.get("otro")).isNotNull().isNotEmpty().hasSize(1);
        assertThat(mapDeudor.get("jaime")).isNotNull().isNotEmpty().hasSize(1);
        assertThat(mapDeudor.get("pedro")).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void deleteById(){
        UUID idToDelete = UUID.randomUUID();
        this.deudasService.deleteById(idToDelete);

        verify(this.deudasRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    public void getAllPixelcoinsDeudasAcredorAndDeudor(){
        when(this.deudasRepository.findByAcredor("jaime")).thenReturn(List.of(
                createDeuda("jaime", "otro"),
                createDeuda("jaime", "pedro")
        ));
        double allPixelcoinsDeudasAcredor = this.deudasService.getAllPixelcoinsDeudasAcredor("jaime");
        assertThat(allPixelcoinsDeudasAcredor).isEqualTo(20);

        when(this.deudasRepository.findByDeudor("jaime")).thenReturn(List.of(
                createDeuda("otro", "jaime"),
                createDeuda("otro", "jaime")
        ));
        double allPixelcoinsDeudasDeudor = this.deudasService.getAllPixelcoinsDeudasDeudor("jaime");
        assertThat(allPixelcoinsDeudasDeudor).isEqualTo(20);
    }

    @Test
    public void findAll(){
        when(this.deudasRepository.findAll()).thenReturn(List.of(
                createDeuda("otro", "jaime"),
                createDeuda("otro", "jaime")
        ));

        assertThat(this.deudasService.findAll()).hasSize(2);
    }

    @Test
    public void findByDeudor(){
        when(this.deudasRepository.findByDeudor("jaime")).thenReturn(List.of(
                createDeuda("otro", "jaime"),
                createDeuda("otro", "jaime")
        ));

        assertThat(this.deudasService.findByDeudor("jaime"))
                .hasSize(2)
                .allMatch(d -> d.getDeudor().equalsIgnoreCase("jaime"));
    }

    @Test
    public void findByAcredor(){
        when(this.deudasRepository.findByAcredor("jaime")).thenReturn(List.of(
                createDeuda("jaime", "otro"),
                createDeuda("jaime", "pedro")
        ));

        assertThat(this.deudasService.findByAcredor("jaime"))
                .hasSize(2)
                .allMatch(d -> d.getAcredor().equalsIgnoreCase("jaime"));
    }

    @Test
    public void getById(){
        UUID deudaIdToFail = UUID.randomUUID();
        when(this.deudasRepository.findById(deudaIdToFail)).thenReturn(Optional.empty());
        assertThatCode(() -> this.deudasService.getById(deudaIdToFail))
                .isInstanceOf(ResourceNotFound.class);
    }

    @Test
    public void save2(){
        var toSave = new Deuda(UUID.randomUUID(), "deudor", "acredor", 10, 5, 0, 2, Funciones.hoy());
        this.deudasService.save(toSave);

        verify(this.deudasRepository, times(1)).save(argThat(of(toSave)));
    }

    @Test
    public void save1(){
        UUID deudaId = this.deudasService.save("deudor", "acredor", 10, 5, 0);
        String fechaHoy = Funciones.DATE_FORMATER_LEGACY.format(new Date());
        int cuota = (int) Math.round((double) 10 / 5);

        verify(this.deudasRepository, times(1)).save(argThat(of(
                new Deuda(deudaId, "deudor", "acredor", 10, 5, 0, cuota, fechaHoy)
        )));
    }
}
