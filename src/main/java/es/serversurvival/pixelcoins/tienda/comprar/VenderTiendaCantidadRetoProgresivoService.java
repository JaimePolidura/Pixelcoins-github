package es.serversurvival.pixelcoins.tienda.comprar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class VenderTiendaCantidadRetoProgresivoService implements RetoProgresivoService {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;

    @Override
    public double getCantidad(UUID jugadorId, Object otro) {
        return jugadoresEstadisticasService.getById(jugadorId)
                .getNVentasTienda();
    }
}
