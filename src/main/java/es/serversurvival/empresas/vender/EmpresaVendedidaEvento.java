package es.serversurvival.empresas.vender;

import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaVendedidaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final double pixelcoins;
    @Getter private final String empresaNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, (int) pixelcoins, empresaNombre, TipoTransaccion.EMPRESA_VENTA);
    }
}
