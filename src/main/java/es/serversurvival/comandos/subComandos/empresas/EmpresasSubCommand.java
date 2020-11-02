package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.Empresas;

public abstract class EmpresasSubCommand extends SubComando {
    private final String cnombre = "empresas";

    public String getCNombre() {
        return cnombre;
    }
}
