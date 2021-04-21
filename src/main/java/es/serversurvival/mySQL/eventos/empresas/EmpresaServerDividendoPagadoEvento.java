package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaServerDividendoPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), empresa, jugador, (int) pixelcoins, "", TipoTransaccion.EMPRESA_DIVIDENDO_ACCION);
    }
}
