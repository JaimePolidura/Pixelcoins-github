package es.serversurvival.jugadores.pagar;

import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class JugadorPagoManualEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String pagador;
    @Getter private final String pagado;
    @Getter private final double cantidad;
    
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), pagador, pagado, (int) cantidad, "", TipoTransaccion.JUGADOR_PAGO_MANUAL);
    }
}
