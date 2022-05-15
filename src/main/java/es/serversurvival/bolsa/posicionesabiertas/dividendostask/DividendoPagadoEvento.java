package es.serversurvival.bolsa.posicionesabiertas.dividendostask;

import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DividendoPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final UUID posicionAbiertaId;
    @Getter private final String jugador;
    @Getter private final String ticker;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, jugador, (int) pixelcoins, "", TipoTransaccion.BOLSA_DIVIDENDO);
    }
}
