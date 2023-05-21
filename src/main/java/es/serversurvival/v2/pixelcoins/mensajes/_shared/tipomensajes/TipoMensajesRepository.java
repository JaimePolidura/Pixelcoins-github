package es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes;

import java.util.Optional;

public interface TipoMensajesRepository {
    Optional<TipoMensaje> findById(int tipoMensajeId);
}
