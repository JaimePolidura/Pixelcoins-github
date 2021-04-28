package es.serversurvival.deudas.pagar;

import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaPagadaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), deudor, acredor, (int) pixelcoins, "", TipoTransaccion.DEUDAS_PAGAR_TODADEUDA);
    }
}
