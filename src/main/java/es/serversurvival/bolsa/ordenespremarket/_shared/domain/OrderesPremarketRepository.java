package es.serversurvival.bolsa.ordenespremarket._shared.domain;

import java.util.List;
import java.util.UUID;

public interface OrderesPremarketRepository {
    void save(OrdenPremarket orden);

    List<OrdenPremarket> findAll();

    List<OrdenPremarket> findByJugador(String jugador);

    void deleteById(UUID id);
}
