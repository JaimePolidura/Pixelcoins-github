package es.serversurvival.mySQL.eventos.tienda;

import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class ItemCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String vendedor;
    @Getter private final String comprador;
    @Getter private final String objeto;
    @Getter private final int cantidadComprada;
    @Getter private final double precioUnidad;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, cantidadComprada, objeto, TIENDA_VENTA);
    }
}
