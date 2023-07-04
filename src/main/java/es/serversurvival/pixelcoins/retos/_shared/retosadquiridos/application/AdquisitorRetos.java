package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.retos.RetoAdquiridoEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.RecompensadorReto;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain.RetoAdquirido;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdquisitorRetos {
    private final RetosAdquiridosService retosAdquiridosService;
    private final DependenciesRepository dependenciesRepository;
    private final EventBus eventBus;

    public void adquirir(UUID jugadorId, List<Reto> retos) {
        for (Reto reto : retos) {
            adquirir(jugadorId, reto);
        }
    }

    public void adquirir(UUID jugadorId, Reto reto) {
        retosAdquiridosService.save(RetoAdquirido.of(jugadorId, reto.getRetoId()));
        recompensar(jugadorId, reto);

        eventBus.publish(new RetoAdquiridoEvento(jugadorId, reto));
    }

    private void recompensar(UUID jugadorId, Reto reto) {
        RecompensadorReto recompensadorReto = dependenciesRepository.get(reto.getTipoRecompensa().getRecompensador());
        recompensadorReto.recompensar(jugadorId, reto);
    }
}
