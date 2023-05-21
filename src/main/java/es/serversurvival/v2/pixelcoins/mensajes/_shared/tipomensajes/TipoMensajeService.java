package es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class TipoMensajeService {
    private final TipoMensajesRepository tipoMensajesRepository;

    public TipoMensaje getById(int tipoMensajeId) {
        return this.tipoMensajesRepository.findById(tipoMensajeId)
                .orElseThrow(() -> new ResourceNotFound(String.format("Mensaje con id %s no encontrado", tipoMensajeId)));
    }
}
