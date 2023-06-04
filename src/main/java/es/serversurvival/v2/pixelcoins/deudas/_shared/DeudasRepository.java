package es.serversurvival.v2.pixelcoins.deudas._shared;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeudasRepository {
    void save(Deuda deuda);

    Optional<Deuda> findById(UUID id);

    List<Deuda> findByAcredorJugadorId(UUID acredorJugadorId);

    List<Deuda> findByDeudorJugadorId(UUID deudorJugadorId);

    List<Deuda> findByJugadorId(UUID jugadorId);

    List<Deuda> findAll();

    void deleteById(UUID id);
}
