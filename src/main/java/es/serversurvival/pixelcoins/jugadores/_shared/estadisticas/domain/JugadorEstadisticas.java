package es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.UUID;

@AllArgsConstructor
public final class JugadorEstadisticas {
    @Getter private UUID jugadorId;

    @Getter private int nDeudaPagos;
    @Getter private int nDeudaInpagos;

    @Getter private int nVentasTienda;
    @Getter private double valorPixelcoinsVentasTienda;
    @Getter private int nComprasTineda;
    @Getter private double valorPixelcoinsComprasTienda;

    @Getter private int nCompraVentasBolsa;

    @SneakyThrows
    public JugadorEstadisticas incrementar(JugadorTipoContadorEstadistica tipoContador, double incremento) {
        Field field = this.getClass().getDeclaredField(tipoContador.getNombreCampo());
        field.setAccessible(true);

        double valorActual = (double) field.get(this);
        double valorIncrementado = valorActual + incremento;

        field.set(this, valorIncrementado);

        return this;
    }

    @SneakyThrows
    public JugadorEstadisticas incrementar(JugadorTipoContadorEstadistica tipoContador, int incremento) {
        Field field = this.getClass().getDeclaredField(tipoContador.getNombreCampo());
        field.setAccessible(true);

        int valorActual = (int) field.get(this);
        int valorIncrementado = valorActual + incremento;

        field.set(this, valorIncrementado);

        return this;
    }
}
