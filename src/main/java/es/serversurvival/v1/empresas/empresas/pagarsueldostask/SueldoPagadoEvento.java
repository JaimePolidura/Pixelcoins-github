package es.serversurvival.v1.empresas.empresas.pagarsueldostask;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class SueldoPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String empleado;
    @Getter private final UUID empleadoId;
    @Getter private final String empresa;
    @Getter private final double sueldo;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresa, empleado, (int) sueldo, "", TipoTransaccion.EMPRESA_PAGAR_SALARIO);
    }

    public static SueldoPagadoEvento of(UUID empleadoId, String empleado, String empresa, double sueldo){
        return new SueldoPagadoEvento(empleado, empleadoId, empresa, sueldo);
    }
}
