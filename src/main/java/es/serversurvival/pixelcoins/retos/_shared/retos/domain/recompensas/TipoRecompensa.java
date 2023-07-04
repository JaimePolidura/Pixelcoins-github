package es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas;

import es.serversurvival.pixelcoins.retos._shared.retos.application.PixelcoinsRecompensador;
import lombok.Getter;

public enum TipoRecompensa {
    LOOTBOX(null),
    PIXELCOINS(PixelcoinsRecompensador.class);

    @Getter private final Class<? extends RecompensadorReto> recompensador;

    TipoRecompensa(Class<? extends RecompensadorReto> recompensador) {
        this.recompensador = recompensador;
    }
}
