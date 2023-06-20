package es.serversurvival.pixelcoins.transacciones;

import es.dependencyinjector.dependencies.annotations.Service;

import java.util.UUID;

import static es.jaime.javaddd.application.utils.CollectionUtils.*;

@Service
public class TransaccionesService {
    private final TransaccionesBalanceCache cache;
    private final TransaccionesRepository repository;

    public TransaccionesService(TransaccionesRepository transaccionesRepository) {
        this.cache = new TransaccionesBalanceCache();
        this.repository = transaccionesRepository;
    }

    public Transaccion save(Transaccion transaccion) {
        this.repository.save(transaccion);

        cache.update(transaccion);

        return transaccion;
    }

    public double getBalancePixelcoins(UUID entidadId) {
        return cache.get(entidadId,
                entidad -> getSum(this.repository.findByPagadoId(entidadId), Transaccion::getPixelcoins),
                entidad -> getSum(this.repository.findByPagadorId(entidadId), Transaccion::getPixelcoins));
    }
}
