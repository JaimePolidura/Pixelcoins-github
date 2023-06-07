package es.serversurvival.pixelcoins.bolsa.vercarteraresumida;

import es.serversurvival.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CarteraResumidaItem implements Comparable<CarteraResumidaItem> {
    @Getter private final UUID activoBolsaId;
    @Getter private final TipoBolsaApuesta tipoBolsaApuesta;
    @Getter private final double peso;
    @Getter private final double precioMedio;
    @Getter private final double precioActual;
    @Getter private final double valorTotalPosicion;
    @Getter private final int cantidad;

    @Override
    public int compareTo(CarteraResumidaItem other) {
        return (int) (other.peso - peso);
    }
}
