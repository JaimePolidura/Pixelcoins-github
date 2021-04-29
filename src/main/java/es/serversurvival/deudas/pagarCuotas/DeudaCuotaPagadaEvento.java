package es.serversurvival.deudas.pagarCuotas;

import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCuotaPagadaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final int deudaId;
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoinsPagadas;
    @Getter private final int tiempoRestante;
    
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), deudor, acredor, (int) pixelcoinsPagadas, "", TipoTransaccion.DEUDAS_PAGAR_CUOTA);
    }
}
