package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;

import java.util.Optional;

public interface RetosRepository {
    Optional<Reto> findById(int retoId);
}
