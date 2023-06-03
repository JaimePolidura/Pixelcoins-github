package es.serversurvival.v2.pixelcoins.bolsa.cancelarordenpremarket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarOrdenPremarketParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID ordenPremarketId;
}
