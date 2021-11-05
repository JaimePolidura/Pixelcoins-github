package es.serversurvival.empresas.editarnombre;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class EditarNombreUseCase implements AllMySQLTablesInstances {
    public static final EditarNombreUseCase INSTANCE = new EditarNombreUseCase();

    private EditarNombreUseCase () {}

    public void editar (String antiguoNombre, String nuevoNombre) {
        empresasMySQL.setNombre(antiguoNombre, nuevoNombre);

        Pixelcoin.publish(new EmpresaNombreEditadoEvento(antiguoNombre, nuevoNombre));
    }
}
