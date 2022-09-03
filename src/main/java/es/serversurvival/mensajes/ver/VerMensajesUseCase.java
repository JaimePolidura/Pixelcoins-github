package es.serversurvival.mensajes.ver;

import es.dependencyinjector.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.mensajes._shared.application.MensajesService;
import es.serversurvival.mensajes._shared.domain.Mensaje;

import java.util.List;

@UseCase
public final class VerMensajesUseCase {
    private final MensajesService mensajesService;

    public VerMensajesUseCase () {
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    public List<Mensaje> getMensajes(String jugador) {
        List<Mensaje> mensajes = this.mensajesService.findMensajesByDestinatario(jugador);

        this.mensajesService.deleteByDestinatario(jugador);

        return mensajes;
    }
}
