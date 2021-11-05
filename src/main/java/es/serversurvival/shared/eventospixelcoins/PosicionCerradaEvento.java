package es.serversurvival.shared.eventospixelcoins;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class PosicionCerradaEvento extends PixelcoinsEvento implements EventoTipoTransaccion, EventoTipoPosicionCerrada {
    @Getter protected final String vendedor;
    @Getter protected final String ticker;
    @Getter protected final String nombreValor;
    @Getter protected final double precioApertura;
    @Getter protected final String fechaApertura;
    @Getter protected final double precioCierre;
    @Getter protected final int cantidad;
    @Getter protected final double rentabilidad;
    @Getter protected final TipoActivo tipoActivo;
}
