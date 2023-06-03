package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AbrirOrdenPremarketCerrarParametros {
    @Getter private final UUID posicionId;
    @Getter private final int cantidadACerrar;
}
