package es.serversurvival.bolsa.activosinfo._shared;

import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.bolsa.activosinfo.DepencyContainerTipoActivoInfoMocks;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static es.serversurvival.bolsa.activosinfo.ActivosInfoTestMother.createActivoInfoAcciones;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ActivosInfoServiceTest {
    @Mock private ActivoInfoRepository repositoryDb;
    private ActivosInfoService service;

    static  {
        DepencyContainerTipoActivoInfoMocks.loadDepencyContainer();
    }

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        UnlimitedCacheSize<String, ActivoInfo> cache = new UnlimitedCacheSize<>();
        this.service = new ActivosInfoService(this.repositoryDb, cache);

        cache.put("AMZN", createActivoInfoAcciones("AMZN"));
        cache.put("GOOG", createActivoInfoAcciones("GOOG"));
    }

    @Test
    public void findByNombreActivo(){
        var exists1 = this.service.existsByNombreActivo("activo");
        assertThat(exists1).isFalse();

        var exists2 = this.service.existsByNombreActivo("AMZN");
        assertThat(exists2).isTrue();
    }

    @Test
    public void deleteByNombreActivo(){
        this.service.deleteByNombreActivo("activo");
        verify(this.repositoryDb, times(1)).deleteByNombreActivo("activo");
    }

    @Test
    public void findAllToMap(){
        assertThat(this.service.findAllToMap()).isNotNull().hasSize(2);
        assertThat(this.service.findAllToMap().get("AMZN")).isNotNull();
        assertThat(this.service.findAllToMap().get("GOOG")).isNotNull();
    }

    @Test
    public void findAll(){
        assertThat(this.service.findAll()).hasSize(2);
    }

    @Test
    public void save(){
        ActivoInfo toSave = createActivoInfoAcciones("activo");
        this.service.save(createActivoInfoAcciones("activo"));
        verify(this.repositoryDb, times(1)).save(toSave);
    }

    @Test
    public void getByNombreActivo(){
        ActivoInfo activoInfo = this.service.getByNombreActivo("AMZN", TipoActivo.ACCIONES);
        assertThat(activoInfo).isNotNull().matches(a -> a.getNombreActivo().equalsIgnoreCase("AMZN"));

        var activoInfoAPICall = this.service.getByNombreActivo("AMZN", TipoActivo.ACCIONES);
        assertThat(activoInfoAPICall.getNombreActivo()).isEqualTo("AMZN");
        assertThat(activoInfoAPICall.getTipoActivo() == TipoActivo.ACCIONES);
        assertThat(activoInfoAPICall.getPrecio() > 0);
    }
}
