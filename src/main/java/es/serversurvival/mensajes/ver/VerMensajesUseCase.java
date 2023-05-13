package es.serversurvival.mensajes.ver;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.mensajes._shared.application.MensajesService;
import es.serversurvival.mensajes._shared.domain.Mensaje;
import lombok.AllArgsConstructor;

import java.util.List;

@UseCase
@AllArgsConstructor
public final class VerMensajesUseCase {
    private final MensajesService mensajesService;

    public List<Mensaje> getMensajes(String jugador) {
        List<Mensaje> mensajes = this.mensajesService.findMensajesByDestinatario(jugador);

        this.mensajesService.deleteByDestinatario(jugador);

        return mensajes;
    }
}
