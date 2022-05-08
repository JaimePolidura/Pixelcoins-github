package es.serversurvival.empresas.empleados.despedir;

import es.serversurvival.empresas.empleados._shared.mysql.Empleado;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.enviarMensaje;

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
