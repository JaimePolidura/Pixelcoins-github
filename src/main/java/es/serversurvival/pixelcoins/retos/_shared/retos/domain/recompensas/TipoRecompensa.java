package es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas;

import es.serversurvival.pixelcoins.lootbox.LootboxRecompensador;
import es.serversurvival.pixelcoins.retos._shared.retos.application.PixelcoinsRecompensador;
import lombok.Getter;

public enum TipoRecompensa {
    LOOTBOX(LootboxRecompensador.class),
    PIXELCOINS(PixelcoinsRecompensador.class);

    @Getter private final Class<? extends RecompensadorReto> recompensador;

    TipoRecompensa(Class<? extends RecompensadorReto> recompensador) {
        this.recompensador = recompensador;
    }
}
