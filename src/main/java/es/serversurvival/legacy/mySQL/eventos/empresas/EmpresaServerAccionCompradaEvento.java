package es.serversurvival.legacy.mySQL.eventos.empresas;

import es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
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
