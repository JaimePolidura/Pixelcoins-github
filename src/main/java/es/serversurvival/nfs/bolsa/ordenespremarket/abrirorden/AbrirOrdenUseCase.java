package es.serversurvival.nfs.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.bolsa.ordenespremarket.mysql.AccionOrden;

public final class AbrirOrdenUseCase implements AllMySQLTablesInstances {
    public static final AbrirOrdenUseCase INSTANCE = new AbrirOrdenUseCase();

    private AbrirOrdenUseCase () {}

    public void abrirOrdenCompraLargo(String playerName, String ticker, int cantidad) {
        ordenesMySQL.nuevaOrden(playerName, ticker, cantidad, AccionOrden.LARGO_COMPRA);
    }
}
