package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenPremarketAbierta extends PixelcoinsEvento {
    @Getter private final UUID ordernPremarketId;
}
