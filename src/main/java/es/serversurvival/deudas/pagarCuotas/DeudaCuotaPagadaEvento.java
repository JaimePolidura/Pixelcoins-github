package es.serversurvival.deudas.pagarCuotas;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@AllArgsConstructor
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
}
