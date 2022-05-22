package es.serversurvival.bolsa.activosinfo;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import lombok.SneakyThrows;

import static org.mockito.Mockito.mock;

public final class DepencyContainerTipoActivoInfoMocks {
    public static boolean hasLoaded = false;

    public static AccionesApiService accionesApiService;
    public static MateriasPrimasApiService materiasPrimasApiService;
    public static CriptomonedasApiService criptomonedasApiService;

    static  {
        loadDepencyContainer();
    }

    @SneakyThrows
    public static void loadDepencyContainer() {
        if(hasLoaded) return;

        accionesApiService = mock(AccionesApiService.class);
        materiasPrimasApiService = mock(MateriasPrimasApiService.class);
        criptomonedasApiService = mock(CriptomonedasApiService.class);

        DependecyContainer.add(AccionesApiService.class, accionesApiService);
        DependecyContainer.add(MateriasPrimasApiService.class, materiasPrimasApiService);
        DependecyContainer.add(CriptomonedasApiService.class, criptomonedasApiService);

        hasLoaded = true;
    }

}
