package es.serversurvival.nfs.empresas.editarnombre;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;

public final class EditarNombreUseCase implements AllMySQLTablesInstances {
    public static final EditarNombreUseCase INSTANCE = new EditarNombreUseCase();

    private EditarNombreUseCase () {}

    public void editar (String antiguoNombre, String nuevoNombre) {
        empresasMySQL.setNombre(antiguoNombre, nuevoNombre);

        Pixelcoin.publish(new EmpresaNombreEditadoEvento(antiguoNombre, nuevoNombre));
    }
}
