package es.serversurvival.bolsa.ordenespremarket.cancelarorden;

import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class CancelarOrdenUseCase implements AllMySQLTablesInstances {
    public static final CancelarOrdenUseCase INSTANCE = new CancelarOrdenUseCase();

    private CancelarOrdenUseCase () {}

    public void cancelar (int id) {
        ordenesMySQL.borrarOrden(id);
    }
}
