package es.serversurvival.v1.bolsa.ordenespremarket._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderesPremarketRepository {
    void save(OrdenPremarket orden);

    Optional<OrdenPremarket> findById(UUID ordenId);

    List<OrdenPremarket> findAll();

    List<OrdenPremarket> findByJugador(String jugador);

    void deleteById(UUID id);
}
