package es.serversurvival.v2.pixelcoins.bolsa.cerrar;

import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application.AbrirOrdenPremarketCerrarParametros;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CerrarPosicionParametros {
    @Getter private final UUID jugadorId;
    @Getter private final int cantidad;
    @Getter private final UUID posicionAbiertaId;

    public AbrirOrdenPremarketCerrarParametros toAbrirOrdenPremarketCerrarParametros() {
        return new AbrirOrdenPremarketCerrarParametros(posicionAbiertaId, cantidad);
    }
}
