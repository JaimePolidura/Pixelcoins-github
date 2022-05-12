package es.serversurvival.bolsa.posicionesabiertas._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosicionesAbiertasRepository {
    void save(PosicionAbierta posicionAbierta);

    Optional<PosicionAbierta> findById(UUID posicionAbiertaId);

    List<PosicionAbierta> findByJugador(String jugador);

    List<PosicionAbierta> findAll();

    void deleteById(UUID posicionAbiertaId);
}
