package es.serversurvival.pixelcoins.lootbox.abrir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemSeleccionadaoResultado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class LootboxItemAbierto extends PixelcoinsEvento {
    @Getter private final LootboxItemSeleccionadaoResultado lootboxItemSeleccionadaoResultado;
    @Getter private final UUID jugadorId;
}
