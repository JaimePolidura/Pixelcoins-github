package es.serversurvival.empresas.ofertasaccionesserver.comprarofertasmercadoserver;

import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaServerAccionCompradaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final double pixelcoins;
    @Getter private final int cantidad;
    @Getter private final OfertaAccionServer oferta;
    @Getter private final String empresaNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), jugador, empresaNombre, (int) pixelcoins, "", TipoTransaccion.EMPRESA_COMPRA_ACCION_JUGADOR);
    }
}
