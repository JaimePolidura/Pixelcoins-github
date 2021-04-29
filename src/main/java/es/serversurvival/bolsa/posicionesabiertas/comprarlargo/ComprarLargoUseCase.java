package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class ComprarLargoUseCase implements AllMySQLTablesInstances {
    public static final ComprarLargoUseCase INSTANCE = new ComprarLargoUseCase();

    private ComprarLargoUseCase() {}

    public void abrir (TipoActivo tipoActivo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String nombrePlayer) {
        posicionesAbiertasMySQL.nuevaPosicion(nombrePlayer, tipoActivo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);

        Pixelcoin.publish(new PosicionCompraLargoEvento(nombrePlayer, precioUnidad, cantidad, cantidad*precioUnidad, ticker, tipoActivo, nombreValor, alias));
    }
}
