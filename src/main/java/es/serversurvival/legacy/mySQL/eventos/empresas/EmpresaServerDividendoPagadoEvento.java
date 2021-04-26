package es.serversurvival.legacy.mySQL.eventos.empresas;

import es.serversurvival.legacy.mySQL.enums.TipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
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
