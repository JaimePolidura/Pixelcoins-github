package es.serversurvival.pixelcoins.retos._shared.retos;

import java.util.Optional;

public interface RetosRepository {
    Optional<Reto> findById(int retoId);
}
