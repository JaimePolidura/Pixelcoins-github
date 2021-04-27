package es.serversurvival.nfs.empresas.borrar;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.empresas.mysql.Empresa;

public final class EmpresaBorrarUseCase implements AllMySQLTablesInstances {
    public static final EmpresaBorrarUseCase INSTANCE = new EmpresaBorrarUseCase();

    private EmpresaBorrarUseCase () {}

    public void borrar (String owner, String empresaNombre) {
        Empresa empresaABorrar = empresasMySQL.getEmpresa(empresaNombre);

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins()));

        empresasMySQL.borrarEmpresa(empresaNombre);
    }
}
