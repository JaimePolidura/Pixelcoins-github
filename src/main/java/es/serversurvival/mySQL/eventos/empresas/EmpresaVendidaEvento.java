package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class EmpresaVendidaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, (int) pixelcoins, empresa, EMPRESA_VENTA);
    }
}
