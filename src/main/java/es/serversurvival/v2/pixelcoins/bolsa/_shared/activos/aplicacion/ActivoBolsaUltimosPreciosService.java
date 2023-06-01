package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.aplicacion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivosBolsaService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class ActivoBolsaUltimosPreciosService {
    private final Map<UUID, Double> cacheByActivoBolsaId;
    private final Map<String, Double> cacheByNombreCorto;

    private final DependenciesRepository dependencies;
    private final ActivosBolsaService activosBolsaService;

    public ActivoBolsaUltimosPreciosService(DependenciesRepository dependenciesRepository, ActivosBolsaService activosBolsaService) {
        this.dependencies = dependenciesRepository;
        this.activosBolsaService = activosBolsaService;
        this.cacheByActivoBolsaId = new ConcurrentHashMap<>();
        this.cacheByNombreCorto = new ConcurrentHashMap<>();
    }

    public double getUltimoPrecio(String nombreCorto, TipoActivoBolsa tipoActivo) {
        if(cacheByNombreCorto.containsKey(nombreCorto)){
            return cacheByNombreCorto.get(nombreCorto);
        }

        ActivoBolsa activoBolsa = getActivoBolsaFromRepository(nombreCorto, tipoActivo);

        getUltimoPrecioYGuardarEnCache(activoBolsa);

        return cacheByNombreCorto.get(nombreCorto);
    }


    public double getUltimoPrecio(UUID activoBolsaId) {
        if(cacheByActivoBolsaId.containsKey(activoBolsaId)){
            return cacheByActivoBolsaId.get(activoBolsaId);
        }

        ActivoBolsa activoBolsa = activosBolsaService.getById(activoBolsaId);
        getUltimoPrecioYGuardarEnCache(activoBolsa);

        return cacheByActivoBolsaId.get(activoBolsaId);
    }

    private synchronized void getUltimoPrecioYGuardarEnCache(ActivoBolsa activoBolsa) {
        double ultimoPrecioDesdeAPI = getUltimoPrecioDesdeAPI(activoBolsa.getNombreCorto(), activoBolsa.getTipoActivoBolsa());
        cacheByNombreCorto.put(activoBolsa.getNombreCorto(), ultimoPrecioDesdeAPI);
        cacheByActivoBolsaId.put(activoBolsa.getActivoBolsaId(), ultimoPrecioDesdeAPI);
    }

    private void crearActivoBolsaRepositorio(String nombreCorto, TipoActivoBolsa tipoActivo) {
        String nombreLargo = getNombreLargoDesdeAPI(nombreCorto, tipoActivo);

        this.activosBolsaService.save(ActivoBolsa.builder()
                        .tipoActivoBolsa(tipoActivo)
                        .nomnbreCorto(nombreCorto)
                        .nombreLargo(nombreLargo)
                .build());
    }

    private String getNombreLargoDesdeAPI(String nombreCorto, TipoActivoBolsa tipoActivo) {
        ActivoBolsaInformationAPIService activoInfoServiceClass = dependencies.get(tipoActivo.getActivoInfoService());
        return activoInfoServiceClass.getNombreLargo(nombreCorto);
    }

    private double getUltimoPrecioDesdeAPI(String nombreCorto, TipoActivoBolsa tipoActivo) {
        ActivoBolsaInformationAPIService activoInfoServiceClass = dependencies.get(tipoActivo.getActivoInfoService());
        return activoInfoServiceClass.getUltimoPrecio(nombreCorto);
    }


    private ActivoBolsa getActivoBolsaFromRepository(String nombreCorto, TipoActivoBolsa tipoActivo) {
        Optional<ActivoBolsa> activoFromRepositoryOpt = activosBolsaService.findByNombreCortoAndTipoActivo(nombreCorto, tipoActivo);
        ActivoBolsa activoBolsa;
        if(activoFromRepositoryOpt.isEmpty()){
            crearActivoBolsaRepositorio(nombreCorto, tipoActivo);
            activoBolsa = activosBolsaService.getByNombreCortoAndTipoActivo(nombreCorto, tipoActivo);
        }else{
            activoBolsa = activoFromRepositoryOpt.get();
        }

        return activoBolsa;
    }
}
