package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application;

import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AbrirOrdenPremarketAbrirParametros {
    @Getter private final UUID jugadorId;
    @Getter private final int cantidad;
    @Getter private final UUID activoBolsaId;
    @Getter private final TipoBolsaApuesta tipoBolsaApuesta;
}
