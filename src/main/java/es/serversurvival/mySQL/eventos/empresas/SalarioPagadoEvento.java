package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class SalarioPagadoEvento extends TransactionEvent {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double salario;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), empresa, jugador, (int) salario, "", EMPRESA_PAGAR_SALARIO);
    }
}
