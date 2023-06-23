package es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static es.serversurvival._shared.ConfigurationVariables.BOLSA_PRECIOS_CACHE_N_VECES_LECTURA_SIN_ACTUALIZAR;
import static es.serversurvival._shared.ConfigurationVariables.BOLSA_PRECIOS_CACHE_TIEMPO_MS_VALIDOS;

@Service
public final class ActivoBolsaUltimosPreciosService {
    private final Map<UUID, ActivoBolsaPrecioCache> cacheByActivoBolsaId;

    private final DependenciesRepository dependencies;
    private final ActivosBolsaService activosBolsaService;

    public ActivoBolsaUltimosPreciosService(DependenciesRepository dependenciesRepository, ActivosBolsaService activosBolsaService) {
        this.dependencies = dependenciesRepository;
        this.activosBolsaService = activosBolsaService;
        this.cacheByActivoBolsaId = new ConcurrentHashMap<>();
    }

    public double getUltimoPrecio(UUID activoBolsaId) {
        if(cacheValida(activoBolsaId)){
            return cacheByActivoBolsaId.get(activoBolsaId).getUltimoPrecio();
        }

        ActivoBolsa activoBolsa = activosBolsaService.getById(activoBolsaId);
        actualizarUltimoPrecioEnCache(activoBolsa);

        return cacheByActivoBolsaId.get(activoBolsaId).getUltimoPrecio();
    }

    private boolean cacheValida(UUID activoBolsaId) {
        return cacheByActivoBolsaId.containsKey(activoBolsaId) &&
                cacheByActivoBolsaId.get(activoBolsaId).getNumeroLectuasSinActualizar() <= BOLSA_PRECIOS_CACHE_N_VECES_LECTURA_SIN_ACTUALIZAR &&
                cacheByActivoBolsaId.get(activoBolsaId).getUltimaVezActualizdoMs() + BOLSA_PRECIOS_CACHE_TIEMPO_MS_VALIDOS >= System.currentTimeMillis();
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
        ActivoBolsaInformationAPIService activoInfoServiceClass = dependencies.get(tipoActivo.getActivoInfoService());

        return activoInfoServiceClass.getUltimoPrecio(nombreCorto);
    }

    private static class ActivoBolsaPrecioCache {
        private AtomicInteger numeroLectuasSinActualizar;
        private long ultimaVezActualizdoMs;
        private double ultimoPrecio;

        public ActivoBolsaPrecioCache(double ultimoPrecio) {
            this.ultimoPrecio = ultimoPrecio;
            this.numeroLectuasSinActualizar = new AtomicInteger(0);
            this.ultimaVezActualizdoMs = System.currentTimeMillis();
        }

        public void actualizar(double nuevoPrecio) {
            this.numeroLectuasSinActualizar.set(0);
            this.ultimoPrecio = nuevoPrecio;
        }

        public double getUltimoPrecio() {
            this.numeroLectuasSinActualizar.getAndIncrement();
            return this.ultimoPrecio;
        }

        public long getUltimaVezActualizdoMs() {
            return this.ultimaVezActualizdoMs;
        }

        public int getNumeroLectuasSinActualizar() {
            return this.numeroLectuasSinActualizar.get();
        }
    }
}
