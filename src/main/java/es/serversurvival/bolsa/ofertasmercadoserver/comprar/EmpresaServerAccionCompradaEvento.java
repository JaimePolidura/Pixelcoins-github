package es.serversurvival.bolsa.ofertasmercadoserver.comprar;

import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaServerAccionCompradaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final double pixelcoins;
    @Getter private final int cantidad;
    @Getter private final OfertaMercadoServer oferta;
    @Getter private final String empresaNombre;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador, empresaNombre, (int) pixelcoins, "", TipoTransaccion.EMPRESA_COMPRA_ACCION_JUGADOR);
    }
}
