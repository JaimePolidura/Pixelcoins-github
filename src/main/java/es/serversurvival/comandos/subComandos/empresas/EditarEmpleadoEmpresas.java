package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoSueldo;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class EditarEmpleadoEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "editarempleado";
    private final String sintaxis = "/empresas editarempleado <empresa>";
    private final String ayuda = "editar un empleado de tu empresa. El tipo (<tipo>) puede ser: sueldo y tiposueldo, donde el primero cambia el sueldo y el segundo la frecuencia de pago (/ayuda empresario). Y el valor lo que quieres poner";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        if(args.length >= 3)
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas/info/" + args[2] + " /cuenta para registrarse");
        else
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas /cuenta para registrarse");

    }
}
