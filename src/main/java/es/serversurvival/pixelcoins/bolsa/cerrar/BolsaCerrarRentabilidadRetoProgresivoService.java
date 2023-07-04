package es.serversurvival.pixelcoins.bolsa.cerrar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class BolsaCerrarRentabilidadRetoProgresivoService implements RetoProgresivoService {
    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return (double) otro;
    }
}
