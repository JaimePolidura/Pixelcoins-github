package es.serversurvival.v1.empresas.empresas.pagardividendos.eventos;

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
public final class EmpresaServerDividendoPagadoJugador extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresa, jugador, (int) pixelcoins, "", TipoTransaccion.EMPRESA_DIVIDENDO_ACCION);
    }

    public static EmpresaServerDividendoPagadoJugador of(String jugador, String empresa, double pixelcoins){
        return new EmpresaServerDividendoPagadoJugador(jugador, empresa, pixelcoins);
    }
}
