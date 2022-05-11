package es.serversurvival.bolsa.other.cancelarorderpremarket;

import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class CancelarOrdenUseCase implements AllMySQLTablesInstances {
    public static final CancelarOrdenUseCase INSTANCE = new CancelarOrdenUseCase();

    private CancelarOrdenUseCase () {}

    public void cancelar (int id) {
        ordenesMySQL.borrarOrden(id);
    }
}
