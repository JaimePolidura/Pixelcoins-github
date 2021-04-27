package es.serversurvival.nfs.shared.eventospixelcoins;

import es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo;
import lombok.Getter;

public abstract class PosicionAbiertaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter protected final String comprador;
    @Getter protected final String ticker;
    @Getter protected final int cantidadPosicion;
    @Getter protected final double precioUnidad;
    @Getter protected final TipoActivo tipoActivo;
    @Getter protected final double precioTotal;
    @Getter protected final String nombreValor;

    public PosicionAbiertaEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                 TipoActivo tipoActivo, String nombreValor) {
        this.cantidadPosicion = cantidad;
        this.tipoActivo = tipoActivo;
        this.precioUnidad = precioUnidad;
        this.ticker = ticker;
        this.precioTotal = precioTotal;
        this.nombreValor = nombreValor;
        this.comprador = comprador;
    }
}
