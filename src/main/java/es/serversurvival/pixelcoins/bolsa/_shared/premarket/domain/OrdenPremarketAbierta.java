package es.serversurvival.pixelcoins.bolsa._shared.premarket.domain;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenPremarketAbierta extends PixelcoinsEvento {
    @Getter private final UUID ordernPremarketId;
}
