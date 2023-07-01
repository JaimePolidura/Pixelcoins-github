package es.serversurvival.pixelcoins.transacciones;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class MovimientosService {
    private final MovimientoRepository movimientoRepository;

    public List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit) {
        return movimientoRepository.findByEntidadIdOrderByFecha(entidadId, limit);
    }
}
