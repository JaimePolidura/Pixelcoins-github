package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class AbrirOrdenUseCase implements AllMySQLTablesInstances {
    public static final AbrirOrdenUseCase INSTANCE = new AbrirOrdenUseCase();

    private AbrirOrdenUseCase () {}

    public void abrirOrdenCompraLargo(String playerName, String ticker, int cantidad) {
        ordenesMySQL.nuevaOrden(playerName, ticker, cantidad, AccionOrden.LARGO_COMPRA);
    }

    public void abrirOrdenCompraCorto(String playerName, String ticker, int cantidad) {
        ordenesMySQL.nuevaOrden(playerName, ticker, cantidad, AccionOrden.CORTO_VENTA);
    }
}
