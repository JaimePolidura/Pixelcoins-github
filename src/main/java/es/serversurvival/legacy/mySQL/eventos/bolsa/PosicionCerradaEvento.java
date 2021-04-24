package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.legacy.mySQL.enums.TipoActivo;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoPosicionCerrada;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
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
