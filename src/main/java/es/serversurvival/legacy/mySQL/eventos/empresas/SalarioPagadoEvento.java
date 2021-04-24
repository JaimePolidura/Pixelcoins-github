package es.serversurvival.legacy.mySQL.eventos.empresas;

import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.legacy.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class SalarioPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double salario;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), empresa, jugador, (int) salario, "", EMPRESA_PAGAR_SALARIO);
    }
}
