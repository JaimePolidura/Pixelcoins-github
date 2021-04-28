package es.serversurvival.empresas.editardescripccion;

import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class EditarDescUseCase implements AllMySQLTablesInstances {
    public static final EditarDescUseCase INSTANCE = new EditarDescUseCase();

    private EditarDescUseCase () {}

    public void edit (String empresa, String descripccion) {
        empresasMySQL.setDescripcion(empresa, descripccion);
    }
}
