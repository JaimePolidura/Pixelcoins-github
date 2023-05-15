package es.serversurvival.v1.deudas.pagarCuotas;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;


@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class DeudaCuotaPagadaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final UUID deudaId;
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoinsPagadas;
    @Getter private final int tiempoRestante;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), deudor, acredor, (int) pixelcoinsPagadas, "", TipoTransaccion.DEUDAS_PAGAR_CUOTA);
    }

    public static DeudaCuotaPagadaEvento of (UUID deudaId, String acredor, String deudor, double pixelcoinsPagadas, int tiempoRestante){
        return new DeudaCuotaPagadaEvento(deudaId, acredor, deudor, pixelcoinsPagadas, tiempoRestante);
    }
}
