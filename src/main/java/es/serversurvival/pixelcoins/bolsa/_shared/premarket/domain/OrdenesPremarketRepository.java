package es.serversurvival.pixelcoins.bolsa._shared.premarket.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdenesPremarketRepository {
    void save(OrdenPremarket orden);

    Optional<OrdenPremarket> findById(UUID ordenPremarketId);

    List<OrdenPremarket> findByJugadorId(UUID jugadorId);

    List<OrdenPremarket> findByPosicionAbiertaId(UUID posicionAbiertaId);

    List<OrdenPremarket> findAll();

    void deleteById(UUID ordenPremarketId);

    void deletebyPosicionAbiertId(UUID posicionAbiertaId);
}
