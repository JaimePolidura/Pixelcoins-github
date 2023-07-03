package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import java.util.UUID;

public interface RetoProgresivoService {
    double getCantidad(UUID jugadorId, Object otro);
}
