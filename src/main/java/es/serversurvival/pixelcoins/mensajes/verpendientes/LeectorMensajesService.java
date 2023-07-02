package es.serversurvival.pixelcoins.mensajes.verpendientes;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.javaddd.domain.database.TransactionManager;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@AllArgsConstructor
public final class LeectorMensajesService {
    private final MensajesService mensajesService;
    private final TransactionManager transactionManager;

    public List<Mensaje> verPendientes(UUID jugadorId) {
        try {
            transactionManager.start();
            List<Mensaje> toReturn = mensajesService.findByJugadorIdNoVistos(jugadorId).stream()
                    .parallel()
                    .peek(mensaje -> mensajesService.save(mensaje.marcarComoVisto()))
                    .sorted(Mensaje.sortByMostRecentFechaEnvio())
                    .collect(Collectors.toList());
            transactionManager.commit();

            return toReturn;
        } catch (Exception e) {
            transactionManager.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getNumeroMensajesPendientes(UUID jugadorId) {
        return mensajesService.findByJugadorIdNoVistos(jugadorId)
                .size();
    }
}
