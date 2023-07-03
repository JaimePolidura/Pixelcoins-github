package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetoAdquirido;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdquisitorRetos {
    private final RetosAdquiridosService retosAdquiridosService;

    public void adquirir(UUID jugadorId, List<Integer> retosId) {
        for (Integer retoId : retosId) {
            retosAdquiridosService.save(RetoAdquirido.of(jugadorId, retoId));
        }
    }

    public void adquirir(UUID jugadorId, Integer retoId) {
        retosAdquiridosService.save(RetoAdquirido.of(jugadorId, retoId));
    }
}
