package es.serversurvival.v2.pixelcoins.bolsa.abrir;

import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application.AbrirOrdenPremarketAbrirParametros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class AbrirPosicoinBolsaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final TipoBolsaApuesta tipoApuesta;
    @Getter private final UUID activoBolsaId;
    @Getter private final int cantidad;

    public AbrirOrdenPremarketAbrirParametros toAbrirOrdenPremarketAbrirParametros() {
        return new AbrirOrdenPremarketAbrirParametros(jugadorId, cantidad, activoBolsaId, tipoApuesta);
    }
}
