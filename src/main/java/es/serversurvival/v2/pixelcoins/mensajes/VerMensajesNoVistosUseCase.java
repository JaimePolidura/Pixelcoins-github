package es.serversurvival.v2.pixelcoins.mensajes;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.mensajes._shared.Mensaje;
import es.serversurvival.v2.pixelcoins.mensajes._shared.MensajesService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@AllArgsConstructor
public final class VerMensajesNoVistosUseCase {
    private final MensajesService mensajesService;

    public List<Mensaje> ver(UUID jugadorId) {
        return mensajesService.findByJugadorIdNoVistos(jugadorId).stream()
                .parallel()
                .peek(mensaje -> mensajesService.save(mensaje.marcarComoVisto()))
                .sorted(Mensaje.sortByMostRecentFechaEnvio())
                .collect(Collectors.toList());
    }
}
