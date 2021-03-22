package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas editarempleado")
public class EditarEmpleadoEmpresas extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editarempleado <empresa>";

    @Override
    public void execute(CommandSender player, String[] args) {
        if(args.length >= 3)
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas/info/" + args[2] + " /cuenta para registrarse");
        else
            player.sendMessage(ChatColor.GOLD + "Para editar empleados: http://serversurvival.ddns.net/profile/empresas /cuenta para registrarse");

    }
}
