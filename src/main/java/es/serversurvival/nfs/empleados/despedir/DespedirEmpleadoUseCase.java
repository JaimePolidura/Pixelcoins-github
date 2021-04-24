package es.serversurvival.nfs.empleados.despedir;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empleado;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import static es.serversurvival.legacy.util.Funciones.enviarMensaje;

public final class DespedirEmpleadoUseCase implements AllMySQLTablesInstances {
    public static final DespedirEmpleadoUseCase INSTANCE = new DespedirEmpleadoUseCase();

    private DespedirEmpleadoUseCase () {}

    public void despedir (String empleado, String empresa, String razon) {
        Empleado empleadoABorrar = empleadosMySQL.getEmpleado(empleado, empresa);
        empleadosMySQL.borrarEmplado(empleadoABorrar.getId());

        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + empresa + " razon: " + razon;

        enviarMensaje(empleado, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
