package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.objetos.mySQL.Empleados;

public abstract class EmpleosSubCommand extends SubComando {
    protected Empleados empleadosMySQL = new Empleados();
    private final String cnombre = "empleos";

    public String getCNombre() {
        return cnombre;
    }
}
