package es.serversurvival.empresas.empresas.borrar;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class EmpresaBorrada extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;
    @Getter private final List<String> antiguosEmpleadosNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), jugador, jugador, (int) pixelcoins, "", EMPRESA_BORRAR);
    }

    public static EmpresaBorrada of(String jugador, String empresa, double pixelcoins, List<String> antiguosEmpleadosNombre){
        return new EmpresaBorrada(jugador, empresa, pixelcoins, antiguosEmpleadosNombre);
    }
}
