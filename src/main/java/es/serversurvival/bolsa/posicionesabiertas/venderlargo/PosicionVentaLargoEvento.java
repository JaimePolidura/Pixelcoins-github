package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.Getter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class PosicionVentaLargoEvento extends PosicionCerradaEvento {
    @Getter private final double valorTotal;
    @Getter private final double resultado;

    public PosicionVentaLargoEvento(String vendedor, String ticker, String nombreValor, double precioApertura,
                                    String fechaApertura, double precioCierre, int cantidad, TipoActivo tipoActivo) {
        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, tipoActivo);

        this.valorTotal = precioCierre * cantidad;
        this.resultado = (precioCierre - precioApertura) * cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosicionVentaLargoEvento that = (PosicionVentaLargoEvento) o;
        return Double.compare(that.valorTotal, valorTotal) == 0 && Double.compare(that.resultado, resultado) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valorTotal, resultado, super.hashCode());
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, vendedor, (int) valorTotal, "", TipoTransaccion.BOLSA_LARGO_VENTA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        String fechaCierre = Funciones.DATE_FORMATER_LEGACY.format(new Date());

        return new PosicionCerrada(UUID.randomUUID(), vendedor, tipoActivo, nombreAcitvo, cantidad, precioApertura, fechaApertura,
                precioCierre, fechaCierre, TipoPosicion.LARGO);
    }
}
