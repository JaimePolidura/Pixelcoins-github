package es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class TipoMensaje {
    @Getter private final int tipoMensajeId;
    @Getter private final TipoContenidoMensaje tipoContenido;
    @Getter private final String mensajeSinFormatear;
}
