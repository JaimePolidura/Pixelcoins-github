package es.serversurvival.v2.pixelcoins.transacciones;

import java.util.List;
import java.util.UUID;

public interface TransaccionesRepository {
    void save(Transaccion transaccion);

    List<Transaccion> findByPagador(UUID pagadorId);

    List<Transaccion> findByPagado(UUID pagadorId);

    List<Transaccion> findByPagadorAndTipo(UUID pagadorId, TipoTransaccion tipo);

    List<Transaccion> findByPagadoAndTipo(UUID pagadoId, TipoTransaccion tipo);
}
