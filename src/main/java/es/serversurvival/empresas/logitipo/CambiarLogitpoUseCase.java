package es.serversurvival.empresas.logitipo;

import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class CambiarLogitpoUseCase implements AllMySQLTablesInstances {
    public static final CambiarLogitpoUseCase INSTANCE = new CambiarLogitpoUseCase();

    private CambiarLogitpoUseCase () {}

    public void cambiar (String empresa, String logotipo) {
        empresasMySQL.setIcono(empresa, logotipo);
    }
}
