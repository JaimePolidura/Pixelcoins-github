package es.serversurvival.v2.pixelcoins.bolsa.premarket;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenPremarket {
    @Getter private final UUID ordenPremarketId;
    @Getter private final UUID jugadorId;
    @Getter private final boolean esCierre;

    @Getter private final int bolsaActivoId;
    @Getter private final int cantidadAbrir;

    @Getter private final UUID posicionAbiertaId;
    @Getter private final int cantidadCerrar;
}
