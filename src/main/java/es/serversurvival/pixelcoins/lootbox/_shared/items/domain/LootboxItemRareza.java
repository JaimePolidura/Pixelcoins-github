package es.serversurvival.pixelcoins.lootbox._shared.items.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum LootboxItemRareza {
    MUY_COMUN(0, 0.35),     // 35 %
    COMUN(0.35, 0.65), // 30 %
    RARO(0.65, 0.85),   // 20 %
    MUY_RARO(0.85, 1);  // 15 %

    private final double min;
    private final double max;

    public double getProbabilidad() {
        return max - min;
    }

    public static LootboxItemRareza getRareza(double probabilidad) {
        return Arrays.stream(LootboxItemRareza.values())
                .filter(rareza -> rareza.min < probabilidad && rareza.max >= probabilidad)
                .findFirst()
                .get();
    }
}
