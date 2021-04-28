package es.serversurvival.empleados.despedir;

import es.serversurvival.empleados.mysql.Empleado;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import static es.serversurvival.utils.Funciones.enviarMensaje;

public final class DespedirEmpleadoUseCase implements AllMySQLTablesInstances {
    public static final DespedirEmpleadoUseCase INSTANCE = new DespedirEmpleadoUseCase();

    private DespedirEmpleadoUseCase () {}

    public void despedir (String empleado, String empresa, String razon) {
        Empleado empleadoABorrar = empleadosMySQL.getEmpleado(empleado, empresa);
        empleadosMySQL.borrarEmplado(empleadoABorrar.getId());

        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + empresa + " razon: " + razon;

        Funciones.enviarMensaje(empleado, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
