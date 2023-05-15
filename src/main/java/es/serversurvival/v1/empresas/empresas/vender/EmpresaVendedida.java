package es.serversurvival.v1.empresas.empresas.vender;

import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaVendedida extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final double pixelcoins;
    @Getter private final String empresaNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, vendedor, (int) pixelcoins, empresaNombre, TipoTransaccion.EMPRESA_VENTA);
    }

    public static EmpresaVendedida of (String comprador, String vendedor, double pixelcoins, String empresaNombre){
        return new EmpresaVendedida(comprador, vendedor, pixelcoins, empresaNombre);
    }
}
