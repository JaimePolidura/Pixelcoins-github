package es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.AbridorOrdenesPremarket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static es.serversurvival._shared.ConfigurationVariables.BOLSA_PRECIOS_CACHE_N_VECES_LECTURA_SIN_ACTUALIZAR;
import static es.serversurvival._shared.ConfigurationVariables.BOLSA_PRECIOS_CACHE_TIEMPO_MS_VALIDOS;

@Service
public final class ActivoBolsaUltimosPreciosService {
    private final Map<UUID, ActivoBolsaPrecioCache> cacheByActivoBolsaId;

    private final AbridorOrdenesPremarket abridorOrdenesPremarket;
    private final ActivosBolsaService activosBolsaService;
    private final DependenciesRepository dependencies;

    public ActivoBolsaUltimosPreciosService(AbridorOrdenesPremarket abridorOrdenesPremarket, DependenciesRepository dependenciesRepository,
                                            ActivosBolsaService activosBolsaService) {
        this.abridorOrdenesPremarket = abridorOrdenesPremarket;
        this.dependencies = dependenciesRepository;
        this.activosBolsaService = activosBolsaService;
        this.cacheByActivoBolsaId = new ConcurrentHashMap<>();
    }

    public double getUltimoPrecio(UUID activoBolsaId, UUID lectorId) {
        if(cacheValida(activoBolsaId, lectorId)){
            return cacheByActivoBolsaId.get(activoBolsaId).getUltimoPrecio(lectorId);
        }

        ActivoBolsa activoBolsa = activosBolsaService.getById(activoBolsaId);
        actualizarUltimoPrecioEnCache(activoBolsa);

        return cacheByActivoBolsaId.get(activoBolsaId).getUltimoPrecio(lectorId);
    }

    private boolean cacheValida(UUID activoBolsaId, UUID lectorId) {
        boolean notCacheInvalidation = lectorId == null || !abridorOrdenesPremarket.estaElMercadoAbierto();

        return cacheByActivoBolsaId.containsKey(activoBolsaId) &&
                (notCacheInvalidation ||
                    (cacheByActivoBolsaId.get(activoBolsaId).getNumeroLectuasSinActualizar(lectorId) <= BOLSA_PRECIOS_CACHE_N_VECES_LECTURA_SIN_ACTUALIZAR &&
                    cacheByActivoBolsaId.get(activoBolsaId).getUltimaVezActualizdoMs() + BOLSA_PRECIOS_CACHE_TIEMPO_MS_VALIDOS >= System.currentTimeMillis()));
    }

    private void actualizarUltimoPrecioEnCache(ActivoBolsa activoBolsa) {
        double ultimoPrecioDesdeAPI = getUltimoPrecioDesdeAPI(activoBolsa.getNombreCorto(), activoBolsa.getTipoActivo());

        if(cacheByActivoBolsaId.containsKey(activoBolsa.getActivoBolsaId())){
            cacheByActivoBolsaId.get(activoBolsa.getActivoBolsaId())
                    .actualizar(ultimoPrecioDesdeAPI);
        } else {
            cacheByActivoBolsaId.put(activoBolsa.getActivoBolsaId(), new ActivoBolsaPrecioCache(ultimoPrecioDesdeAPI));
        }
    }

    private double getUltimoPrecioDesdeAPI(String nombreCorto, TipoActivoBolsa tipoActivo) {
        TipoActivoBolsaService activoInfoServiceClass = dependencies.get(tipoActivo.getActivoInfoService());

        return activoInfoServiceClass.getUltimoPrecio(nombreCorto);
    }

    private static class ActivoBolsaPrecioCache {
        private final Map<UUID, AtomicInteger> lectoresNumeroLectuasSinActualizar;
        private long ultimaVezActualizdoMs;
        private double ultimoPrecio;

        public ActivoBolsaPrecioCache(double ultimoPrecio) {
            this.lectoresNumeroLectuasSinActualizar = new ConcurrentHashMap<>();
            this.ultimaVezActualizdoMs = System.currentTimeMillis();
            this.ultimoPrecio = ultimoPrecio;
        }

        public void actualizar(double nuevoPrecio) {
            this.lectoresNumeroLectuasSinActualizar.keySet()
                    .forEach(key -> lectoresNumeroLectuasSinActualizar.get(key).set(0));
            this.ultimoPrecio = nuevoPrecio;
            this.ultimaVezActualizdoMs = System.currentTimeMillis();
        }

        public double getUltimoPrecio(UUID lectorId) {
            if(lectorId != null){
                lectoresNumeroLectuasSinActualizar.putIfAbsent(lectorId, new AtomicInteger(0));
                lectoresNumeroLectuasSinActualizar.get(lectorId).getAndIncrement();
            }

            return this.ultimoPrecio;
        }

        public long getUltimaVezActualizdoMs() {
            return this.ultimaVezActualizdoMs;
        }

        public int getNumeroLectuasSinActualizar(UUID lectorId) {
            return lectoresNumeroLectuasSinActualizar.getOrDefault(lectorId, new AtomicInteger(0))
                    .get();
        }
    }
}
