package es.serversurvival.pixelcoins.transacciones.domain;

import java.util.List;
import java.util.UUID;

public interface MovimientoRepository {
    void save(Movimiento movimiento);

    double getBalance(UUID entidadId);

    double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId);

    double getBalance(TipoTransaccion tipoTransaccion, UUID entidadId, UUID otro);

    List<Movimiento> findByEntidadIdOrderByFecha(UUID entidadId, int limit);
}
