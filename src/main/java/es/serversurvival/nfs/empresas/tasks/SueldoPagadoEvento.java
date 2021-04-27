package es.serversurvival.nfs.empresas.tasks;

import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class SueldoPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String empleado;
    @Getter private final int empleadoId;
    @Getter private final String empresa;
    @Getter private final double sueldo;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), empresa, empleado, (int) sueldo, "", EMPRESA_PAGAR_SALARIO);
    }
}
