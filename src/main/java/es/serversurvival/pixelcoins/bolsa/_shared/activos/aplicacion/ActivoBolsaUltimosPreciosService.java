package es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static es.jaime.javaddd.application.utils.ConcurrencyUtils.*;

@Service
public final class ActivoBolsaUltimosPreciosService {
    private final Map<UUID, Double> cacheByActivoBolsaId;
    private final Map<String, Double> cacheByNombreCorto;

    private final DependenciesRepository dependencies;
    private final ActivosBolsaService activosBolsaService;
    private final Lock updateCacheLock;

    public ActivoBolsaUltimosPreciosService(DependenciesRepository dependenciesRepository, ActivosBolsaService activosBolsaService) {
        this.dependencies = dependenciesRepository;
        this.activosBolsaService = activosBolsaService;
        this.cacheByActivoBolsaId = new ConcurrentHashMap<>();
        this.cacheByNombreCorto = new ConcurrentHashMap<>();
        this.updateCacheLock = new ReentrantLock();
    }

    public double getUltimoPrecio(String nombreCorto, TipoActivoBolsa tipoActivo) {
        if(cacheByNombreCorto.containsKey(nombreCorto)){
            return cacheByNombreCorto.get(nombreCorto);
        }

        ActivoBolsa activoBolsa = getActivoBolsaFromRepository(nombreCorto, tipoActivo);

        actualizarUltimoPrecioEnCache(activoBolsa);

        return cacheByNombreCorto.get(nombreCorto);
    }


    public double getUltimoPrecio(UUID activoBolsaId) {
        if(cacheByActivoBolsaId.containsKey(activoBolsaId)){
            return cacheByActivoBolsaId.get(activoBolsaId);
        }

        ActivoBolsa activoBolsa = activosBolsaService.getById(activoBolsaId);
        actualizarUltimoPrecioEnCache(activoBolsa);

        return cacheByActivoBolsaId.get(activoBolsaId);
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

    private double getUltimoPrecioDesdeAPI(String nombreCorto, TipoActivoBolsa tipoActivo) {
        ActivoBolsaInformationAPIService activoInfoServiceClass = dependencies.get(tipoActivo.getActivoInfoService());
        return activoInfoServiceClass.getUltimoPrecio(nombreCorto);
    }

    private void actualizarUltimoPrecioEnCache(ActivoBolsa activoBolsa) {
        tryLockOnce(updateCacheLock, () -> {
            double ultimoPrecioDesdeAPI = getUltimoPrecioDesdeAPI(activoBolsa.getNombreCorto(), activoBolsa.getTipoActivoBolsa());

            cacheByNombreCorto.put(activoBolsa.getNombreCorto(), ultimoPrecioDesdeAPI);
            cacheByActivoBolsaId.put(activoBolsa.getActivoBolsaId(), ultimoPrecioDesdeAPI);
        });
    }

    @Task(BukkitTimeUnit.MINUTE * 5)
    public class ActualizadorPreciosTask implements TaskRunner {
        @Override
        public void run() {
            activosBolsaService.findAllNReferenciasMayorQue0().stream()
                    .parallel()
                    .forEach(ActivoBolsaUltimosPreciosService.this::actualizarUltimoPrecioEnCache);
        }
    }
}
