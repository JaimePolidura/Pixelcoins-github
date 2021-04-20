package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class PixelcoinsDepositadasEvento extends TransactionEvent {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador, empresa, (int) pixelcoins, "", EMPRESA_DEPOSITAR);
    }
}
