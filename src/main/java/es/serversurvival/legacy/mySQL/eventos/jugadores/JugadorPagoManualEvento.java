package es.serversurvival.legacy.mySQL.eventos.jugadores;

import es.serversurvival.legacy.mySQL.enums.TipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class JugadorPagoManualEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String pagador;
    @Getter private final String pagado;
    @Getter private final double cantidad;
    
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), pagador, pagado, (int) cantidad, "", TipoTransaccion.JUGADOR_PAGO_MANUAL);
    }
}
