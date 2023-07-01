package es.serversurvival.pixelcoins.transacciones;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovimientoRepository {
    void save(Movimiento movimiento);

    double getBalance(UUID entidadId);

    List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit);
}
