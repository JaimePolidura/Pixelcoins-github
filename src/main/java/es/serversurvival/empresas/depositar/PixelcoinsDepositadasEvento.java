package es.serversurvival.empresas.depositar;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class PixelcoinsDepositadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final Empresa empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador.getNombre(), empresa.getNombre(), (int) pixelcoins, "", EMPRESA_DEPOSITAR);
    }
}
