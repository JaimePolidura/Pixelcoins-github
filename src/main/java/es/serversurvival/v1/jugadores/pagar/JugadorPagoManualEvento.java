package es.serversurvival.v1.jugadores.pagar;

import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@ToString
public final class JugadorPagoManualEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String pagador;
    @Getter private final String pagado;
    @Getter private final double cantidad;
    
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), pagador, pagado, (int) cantidad, "", TipoTransaccion.JUGADOR_PAGO_MANUAL);
    }
}
