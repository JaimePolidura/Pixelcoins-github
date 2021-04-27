package es.serversurvival.nfs.deudas.pagar;

import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class DeudaPagadaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), deudor, acredor, (int) pixelcoins, "", DEUDAS_PAGAR_TODADEUDA);
    }
}
