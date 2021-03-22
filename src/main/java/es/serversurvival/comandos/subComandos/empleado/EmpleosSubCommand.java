package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.comandos.SubComando;
import es.serversurvival.mySQL.Empleados;

public abstract class EmpleosSubCommand extends SubComando {
    private final String cnombre = "empleos";

    public String getCNombre() {
        return cnombre;
    }
}
