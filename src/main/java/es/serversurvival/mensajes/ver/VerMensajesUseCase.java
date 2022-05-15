package es.serversurvival.mensajes.ver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.mensajes._shared.application.MensajesService;
import es.serversurvival.mensajes._shared.domain.Mensaje;

import java.util.List;

public final class VerMensajesUseCase implements AllMySQLTablesInstances {
    private final MensajesService mensajesService;

    private VerMensajesUseCase () {
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    public List<Mensaje> getMensajes(String jugador) {
        List<Mensaje> mensajes = this.mensajesService.findMensajesByDestinatario(jugador);

        this.mensajesService.deleteByDestinatario(jugador);

        return mensajes;
    }
}
