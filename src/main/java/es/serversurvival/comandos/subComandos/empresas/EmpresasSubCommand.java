package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;

public abstract class EmpresasSubCommand extends SubComando {
    protected Empresas empresasMySQL = new Empresas();
    protected Empleados empleadosMySQL = new Empleados();
    private final String cnombre = "empresas";

    public String getCNombre() {
        return cnombre;
    }
}
