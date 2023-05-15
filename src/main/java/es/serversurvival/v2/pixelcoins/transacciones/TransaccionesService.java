package es.serversurvival.v2.pixelcoins.transacciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.cache.MUCache;
import es.jaime.javaddd.application.utils.CollectionUtils;
import es.jaime.javaddd.domain.cache.Cache;

import java.util.Optional;
import java.util.UUID;

import static es.jaime.javaddd.application.utils.CollectionUtils.*;

@Service
public final class TransaccionesService {
    private final TransaccionesRepository repository;
    private final Cache<UUID, Double> cachePixelcoinsPagador;
    private final Cache<UUID, Double> cachePixelcoinsPagado;

    public TransaccionesService(TransaccionesRepository transaccionesRepository) {
        this.repository = transaccionesRepository;
        this.cachePixelcoinsPagador = new MUCache<>(100);
        this.cachePixelcoinsPagado = new MUCache<>(100);
    }

    public Transaccion save(Transaccion transaccion) {
        this.repository.save(transaccion);

        return transaccion;
    }

    public double getBalancePixelcions(UUID algunaEntidadId) {
        return getPixelcoinsPagado(algunaEntidadId) - getPixelcoinsPagador(algunaEntidadId);
    }

    private double getPixelcoinsPagado(UUID pagadoId) {
        return this.cachePixelcoinsPagado.find(pagadoId)
                .or(() -> {
                    double fromDb = getSum(this.repository.findByPagado(pagadoId), Transaccion::getPixelcoins);
                    this.cachePixelcoinsPagado.put(pagadoId, fromDb);
                    return Optional.of(fromDb);
                })
                .get();
    }

    private double getPixelcoinsPagador(UUID pagadorId) {
        return this.cachePixelcoinsPagador.find(pagadorId)
                .or(() -> {
                    double fromDb = getSum(this.repository.findByPagador(pagadorId), Transaccion::getPixelcoins);
                    this.cachePixelcoinsPagador.put(pagadorId, fromDb);
                    return Optional.of(fromDb);
                })
                .get();
    }
}
