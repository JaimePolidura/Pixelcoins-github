package es.serversurvival.nfs.empresas.editardescripccion;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;

public final class EditarDescUseCase implements AllMySQLTablesInstances {
    public static final EditarDescUseCase INSTANCE = new EditarDescUseCase();

    private EditarDescUseCase () {}

    public void edit (String empresa, String descripccion) {
        empresasMySQL.setDescripcion(empresa, descripccion);
    }
}
