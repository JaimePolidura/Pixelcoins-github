package es.serversurvival.pixelcoins.bolsa.vercarteraresumida;

import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.Posicion;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public final class CarteraResumidaItemBuilder {
    @Getter       private UUID activoBolsaId;
    @Getter       private TipoBolsaApuesta tipoBolsaApuesta;
    @Getter @With private double peso;
    @Getter       private double precioMedio;
    @Getter       private double precioActual;
    @Getter       private final double valorPosicion;
    @Getter       private int cantidad;

    public static CarteraResumidaItemBuilder fromPosicion(Posicion posicion, double precioActual, double valorPosicion) {
        return new CarteraResumidaItemBuilder(posicion.getActivoBolsaId(), posicion.getTipoApuesta(), 0,
                posicion.getPrecioApertura(), precioActual, valorPosicion, posicion.getCantidad());
    }

    public CarteraResumidaItemBuilder merge(Posicion posicion, double precioActual, double valorPosicion) {
        int nuevalCantidadTotal = cantidad + posicion.getCantidad();
        double nuevoPrecioMedio = (posicion.getCantidad() * posicion.getPrecioApertura() + cantidad * precioMedio) / nuevalCantidadTotal;
        double nuevoValorPosicion = valorPosicion + this.valorPosicion;

        return new CarteraResumidaItemBuilder(activoBolsaId, tipoBolsaApuesta, peso, nuevoPrecioMedio, nuevoValorPosicion, precioActual, nuevalCantidadTotal);
    }

    public CarteraResumidaItem build() {
        return new CarteraResumidaItem(activoBolsaId, tipoBolsaApuesta, peso, precioMedio, precioActual, precioActual, cantidad);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarteraResumidaItemBuilder that = (CarteraResumidaItemBuilder) o;
        return Objects.equals(activoBolsaId, that.activoBolsaId) && tipoBolsaApuesta == that.tipoBolsaApuesta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(activoBolsaId, tipoBolsaApuesta);
    }
}
