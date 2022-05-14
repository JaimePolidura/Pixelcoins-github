package es.serversurvival._shared.eventospixelcoins;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import lombok.Getter;

public abstract class PosicionAbiertaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter protected final String comprador;
    @Getter protected final String ticker;
    @Getter protected final int cantidadPosicion;
    @Getter protected final double precioUnidad;
    @Getter protected final SupportedTipoActivo tipoActivo;
    @Getter protected final double precioTotal;
    @Getter protected final String nombreActivo;

    public PosicionAbiertaEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                 SupportedTipoActivo tipoActivo, String nombreValor) {
        this.cantidadPosicion = cantidad;
        this.tipoActivo = tipoActivo;
        this.precioUnidad = precioUnidad;
        this.ticker = ticker;
        this.precioTotal = precioTotal;
        this.nombreActivo = nombreValor;
        this.comprador = comprador;
    }
}
