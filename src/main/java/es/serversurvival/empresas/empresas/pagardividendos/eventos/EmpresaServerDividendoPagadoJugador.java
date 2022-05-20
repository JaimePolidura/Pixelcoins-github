package es.serversurvival.empresas.empresas.pagardividendos.eventos;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaServerDividendoPagadoJugador extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresa, jugador, (int) pixelcoins, "", EMPRESA_DIVIDENDO_ACCION);
    }

    public static EmpresaServerDividendoPagadoJugador of(String jugador, String empresa, double pixelcoins){
        return new EmpresaServerDividendoPagadoJugador(jugador, empresa, pixelcoins);
    }
}
