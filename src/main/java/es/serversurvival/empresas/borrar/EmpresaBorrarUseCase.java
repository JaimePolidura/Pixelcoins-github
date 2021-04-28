package es.serversurvival.empresas.borrar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class EmpresaBorrarUseCase implements AllMySQLTablesInstances {
    public static final EmpresaBorrarUseCase INSTANCE = new EmpresaBorrarUseCase();

    private EmpresaBorrarUseCase () {}

    public void borrar (String owner, String empresaNombre) {
        Empresa empresaABorrar = empresasMySQL.getEmpresa(empresaNombre);

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins()));

        empresasMySQL.borrarEmpresa(empresaNombre);
    }
}
