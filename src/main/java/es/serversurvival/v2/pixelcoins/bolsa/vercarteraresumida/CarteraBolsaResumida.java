package es.serversurvival.v2.pixelcoins.bolsa.vercarteraresumida;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public final class CarteraBolsaResumida {
    @Getter private final List<CarteraResumidaItem> items;
    @Getter private final double valorTotalCartera;
}
