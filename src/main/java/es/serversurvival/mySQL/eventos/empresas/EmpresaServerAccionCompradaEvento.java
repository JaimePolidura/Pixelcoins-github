package es.serversurvival.mySQL.eventos.empresas;

import es.serversurvival.mySQL.enums.TipoOfertante;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.mySQL.tablasObjetos.OfertaMercadoServer;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaServerAccionCompradaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final double pixelcoins;
    @Getter private final int cantidad;
    @Getter private final OfertaMercadoServer oferta;
    @Getter private final Empresa empresa;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador, empresa.getNombre(), (int) pixelcoins, "", TipoTransaccion.EMPRESA_COMPRA_ACCION_JUGADOR);
    }
}
