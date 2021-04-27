package es.serversurvival.nfs.empresas.logitipo;

import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;

public final class CambiarLogitpoUseCase implements AllMySQLTablesInstances {
    public static final CambiarLogitpoUseCase INSTANCE = new CambiarLogitpoUseCase();

    private CambiarLogitpoUseCase () {}

    public void cambiar (String empresa, String logotipo) {
        empresasMySQL.setIcono(empresa, logotipo);
    }
}
