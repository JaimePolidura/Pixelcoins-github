package es.serversurvival.nfs.empresas.vender;

import es.serversurvival.legacy.mySQL.enums.TipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpreasVendedidaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final double pixelcoins;
    @Getter private final String empresaNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, (int) pixelcoins, empresaNombre, TipoTransaccion.EMPRESA_VENTA);
    }
}
