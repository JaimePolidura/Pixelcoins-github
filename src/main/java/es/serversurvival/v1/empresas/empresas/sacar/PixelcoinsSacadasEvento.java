package es.serversurvival.v1.empresas.empresas.sacar;

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
public final class PixelcoinsSacadasEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugadorNombre;
    @Getter private final String empresaNombre;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), empresaNombre, jugadorNombre, (int) pixelcoins, "", TipoTransaccion.EMPRESA_SACAR);
    }

    public static PixelcoinsSacadasEvento of(String jugadorNombre, String empresaNombre, double pixelcoins){
        return new PixelcoinsSacadasEvento(jugadorNombre, empresaNombre, pixelcoins);
    }
}
