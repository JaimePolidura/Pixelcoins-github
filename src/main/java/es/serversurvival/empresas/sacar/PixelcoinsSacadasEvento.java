package es.serversurvival.empresas.sacar;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class PixelcoinsSacadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final Empresa empresa;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), empresa.getNombre(), jugador.getNombre(), (int) pixelcoins, "", EMPRESA_SACAR);
    }
}
