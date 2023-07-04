package es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas;

import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;

import java.util.UUID;

public interface RecompensadorReto {
    void recompensar(UUID jugadorId, Reto reto);
}
