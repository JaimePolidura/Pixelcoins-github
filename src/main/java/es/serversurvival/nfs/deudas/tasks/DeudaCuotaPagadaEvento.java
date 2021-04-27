package es.serversurvival.nfs.deudas.tasks;

import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class DeudaCuotaPagadaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final int deudaId;
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoinsPagadas;
    @Getter private final int tiempoRestante;
    
    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), deudor, acredor, (int) pixelcoinsPagadas, "", DEUDAS_PAGAR_CUOTA);
    }
}
