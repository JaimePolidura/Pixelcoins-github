package es.serversurvival.v2.pixelcoins.jugadores._shared.estadisticas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class JugadoresEstadisticasService {
    private final JugadoresEstadisticasRepository repository;

    public void save(JugadorEstadisticas estadisticas) {
        repository.save(estadisticas);
    }

    public JugadorEstadisticas getById(UUID jugadorId) {
        return repository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFound("Estadisitca del jugador no encontrada"));
    }

    public List<JugadorEstadisticas> sortBy(JugadorTipoContadorEstadistica tipoContadorEstadistica, boolean creciente) {
        return repository.sortBy(tipoContadorEstadistica, creciente);
    }

    public List<JugadorEstadisticas> sortBy(JugadorTipoContadorEstadistica tipoContadorEstadistica, boolean creciente, int limit) {
        return repository.sortBy(tipoContadorEstadistica, creciente).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void incrementar(UUID jugadorId, JugadorTipoContadorEstadistica tipoContadorEstadistica, double incremento) {
        JugadorEstadisticas jugadorEstadisticas = getById(jugadorId)
                .incrementar(tipoContadorEstadistica, incremento);

        repository.save(jugadorEstadisticas);
    }

    public void incrementar(UUID jugadorId, JugadorTipoContadorEstadistica tipoContadorEstadistica) {
        JugadorEstadisticas jugadorEstadisticas = getById(jugadorId)
                .incrementar(tipoContadorEstadistica, 1);

        repository.save(jugadorEstadisticas);
    }
}
