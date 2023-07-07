package es.serversurvival.pixelcoins.lootbox.items.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemSeleccionadaoResultado;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItem;
import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxItemRareza;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public final class LootboxItemSeleccionador {
    private final LootboxItemsService lootboxItemsService;

    public LootboxItemSeleccionadaoResultado seleccionarAleatorio(LootboxTier tier) {
        LootboxItemRareza rareza = LootboxItemRareza.getRareza(Math.random());
        List<LootboxItem> candidatos = lootboxItemsService.findByTierAndRareza(tier, rareza);
        LootboxItem itemSeleccionado = candidatos.get((int) (Math.random() * candidatos.size()));
        int cantidad = itemSeleccionado.getCantidadAleatoria();

        return LootboxItemSeleccionadaoResultado.builder()
                .lootboxItemId(itemSeleccionado.getLootboxItemId())
                .nombre(itemSeleccionado.getNombre())
                .encantamientos(itemSeleccionado.getEncantamientos())
                .cantidad(cantidad)
                .build();
    }
}
