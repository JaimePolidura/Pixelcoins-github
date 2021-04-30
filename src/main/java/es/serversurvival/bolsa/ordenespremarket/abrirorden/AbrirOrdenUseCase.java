package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class AbrirOrdenUseCase implements AllMySQLTablesInstances {
    public static final AbrirOrdenUseCase INSTANCE = new AbrirOrdenUseCase();

    private AbrirOrdenUseCase () {}

    public void abrirOrden(String playerName, String ticker, int cantidad, AccionOrden tipoOrden) {
        ordenesMySQL.nuevaOrden(playerName, ticker, cantidad, tipoOrden);
    }
}
