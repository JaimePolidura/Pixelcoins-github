package es.serversurvival.tienda._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TiendaRepository {
    void save(TiendaObjeto tiendaObjeto);

    Optional<TiendaObjeto> findById(UUID id);

    List<TiendaObjeto> findByJugador(String jugador);

    List<TiendaObjeto> findAll();

    void deleteById(UUID id);
}
