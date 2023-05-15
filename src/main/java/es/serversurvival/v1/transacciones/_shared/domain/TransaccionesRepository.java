package es.serversurvival.v1.transacciones._shared.domain;

import java.util.List;

public interface TransaccionesRepository {
    void save(Transaccion transaccion);

    List<Transaccion> findByJugador(String jugador);
}
