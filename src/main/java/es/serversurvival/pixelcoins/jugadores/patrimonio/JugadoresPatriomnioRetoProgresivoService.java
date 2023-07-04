package es.serversurvival.pixelcoins.jugadores.patrimonio;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class JugadoresPatriomnioRetoProgresivoService implements RetoProgresivoService {
    private final CalculadorPatrimonioService calculadorPatrimonioService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return calculadorPatrimonioService.calcular(jugadorId);
    }
}
