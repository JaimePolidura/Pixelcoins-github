package es.serversurvival.nfs.empresas.borrar;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;

public final class EmpresaBorrarUseCase implements AllMySQLTablesInstances {
    public static final EmpresaBorrarUseCase INSTANCE = new EmpresaBorrarUseCase();

    private EmpresaBorrarUseCase () {}

    public void borrar (String owner, String empresaNombre) {
        Empresa empresaABorrar = empresasMySQL.getEmpresa(empresaNombre);

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins()));

        empresasMySQL.borrarEmpresa(empresaNombre);
    }
}
