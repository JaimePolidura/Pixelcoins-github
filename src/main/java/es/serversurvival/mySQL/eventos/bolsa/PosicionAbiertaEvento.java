package es.serversurvival.mySQL.eventos.bolsa;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.TransactionEvent;
import lombok.Getter;

public abstract class PosicionAbiertaEvento extends TransactionEvent {
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
