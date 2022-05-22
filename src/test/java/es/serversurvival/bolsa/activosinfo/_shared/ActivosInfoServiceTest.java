package es.serversurvival.bolsa.activosinfo._shared;

import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoCacheRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.createActivoInfoAcciones;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ActivosInfoServiceTest {
    @Mock private ActivoInfoCacheRepository repositoryCache;
    private ActivosInfoService service;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        this.service = new ActivosInfoService(this.repositoryCache);
    }

    @Test
    public void findByNombreActivo(){
        when(this.repositoryCache.findByNombreActivo("activo")).thenReturn(Optional.empty());
        var exists1 = this.service.existsByNombreActivo("activo");
        assertThat(exists1).isFalse();


        when(this.repositoryCache.findByNombreActivo("activo2")).thenReturn(Optional.of(createActivoInfoAcciones("activo2")));
        var exists2 = this.service.existsByNombreActivo("activo2");
        assertThat(exists2).isTrue();
    }

    @Test
    public void deleteByNombreActivo(){
        this.service.deleteByNombreActivo("activo");
        verify(this.repositoryCache, times(1)).deleteByNombreActivo("activo");
    }

    @Test
    public void findAllToMap(){
        when(this.repositoryCache.findAll()).thenReturn(List.of(
                createActivoInfoAcciones("AMZN"),
                createActivoInfoAcciones("GOOG")
        ));
        assertThat(this.service.findAllToMap()).isNotNull().hasSize(2);
        assertThat(this.service.findAllToMap().get("AMZN")).isNotNull();
        assertThat(this.service.findAllToMap().get("GOOG")).isNotNull();
    }

    @Test
    public void findAll(){
        when(this.repositoryCache.findAll()).thenReturn(List.of(
                createActivoInfoAcciones("AMZN"),
                createActivoInfoAcciones("GOOG")
        ));

        assertThat(this.service.findAll()).hasSize(2);
    }

    @Test
    public void save(){
        ActivoInfo toSave = createActivoInfoAcciones("activo");
        this.service.save(createActivoInfoAcciones("activo"));
        verify(this.repositoryCache, times(1)).save(toSave);
    }

    @Test
    public void getByNombreActivo(){
        ActivoInfo toReturn = createActivoInfoAcciones("AMZN");
        when(this.repositoryCache.findByNombreActivo("AMZN")).thenReturn(
                Optional.of(toReturn)
        );
        ActivoInfo activoInfo = this.service.getByNombreActivo("AMZN", SupportedTipoActivo.ACCIONES);
        assertThat(activoInfo).isNotNull().isEqualTo(toReturn);

        var activoInfoAPICall = this.service.getByNombreActivo("GOOG", SupportedTipoActivo.ACCIONES);
        assertThat(activoInfoAPICall.getNombreActivo()).isEqualTo("GOOG");
        assertThat(activoInfoAPICall.getTipoActivo() == SupportedTipoActivo.ACCIONES);
        assertThat(activoInfoAPICall.getPrecio() > 0);
    }
}
