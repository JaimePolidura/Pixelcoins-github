package es.serversurvival.empresas.empresas.sacar;

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
public final class PixelcoinsSacadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugadorNombre;
    @Getter private final String empresaNombre;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresaNombre, jugadorNombre, (int) pixelcoins, "", EMPRESA_SACAR);
    }

    public static PixelcoinsSacadasEvento of(String jugadorNombre, String empresaNombre, double pixelcoins){
        return new PixelcoinsSacadasEvento(jugadorNombre, empresaNombre, pixelcoins);
    }
}
