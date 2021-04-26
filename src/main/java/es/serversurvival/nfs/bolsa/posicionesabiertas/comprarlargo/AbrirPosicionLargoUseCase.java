package es.serversurvival.nfs.bolsa.posicionesabiertas.comprarlargo;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCompraLargoEvento;
import es.serversurvival.nfs.Pixelcoin;

public final class AbrirPosicionLargoUseCase implements AllMySQLTablesInstances {
    public static final AbrirPosicionLargoUseCase INSTANCE = new AbrirPosicionLargoUseCase();

    private AbrirPosicionLargoUseCase() {}

    public void abrir (TipoActivo tipoActivo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String nombrePlayer) {
        posicionesAbiertasMySQL.nuevaPosicion(nombrePlayer, tipoActivo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);

        Pixelcoin.publish(new PosicionCompraLargoEvento(nombrePlayer, precioUnidad, cantidad, cantidad*precioUnidad, ticker, tipoActivo, nombreValor, alias));
    }
}
