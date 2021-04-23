package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class ServicioCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final Empresa empresa;
    @Getter private final double precio;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, empresa.getNombre(), (int) precio, "", EMPRESA_COMPRAR_SERVICIO);
    }
}
