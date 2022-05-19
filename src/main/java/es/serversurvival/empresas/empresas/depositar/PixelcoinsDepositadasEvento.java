package es.serversurvival.empresas.empresas.depositar;

import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class PixelcoinsDepositadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final Empresa empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), jugador.getNombre(), empresa.getNombre(), (int) pixelcoins, "", EMPRESA_DEPOSITAR);
    }

    public static PixelcoinsDepositadasEvento of(Jugador jugador, Empresa empresa, double pixelcoins){
        return new PixelcoinsDepositadasEvento(jugador, empresa, pixelcoins);
    }
}
