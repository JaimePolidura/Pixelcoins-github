package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoSueldo;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.menus.solicitudes.ContratarSolicitud;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas contratar")
public class ContratarEmpresas extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 5 || args.length == 6, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[3], usoIncorrecto, NaturalNumber)
                .andMayThrowException(() -> args[1], usoIncorrecto, JugadorOnline, NoLeHanEnviadoSolicitud, NoTrabajaEmpresa.en(() -> args[2]), NotEqualsIgnoreCase.of(player.getName(), "No te puedes contratar a ti mismo"))
                .andMayThrowException(() -> TipoSueldo.codigoCorrecto(args[4]), usoIncorrecto, True.of("El tipo de sueldo solo puede ser d: cdda dia, s: cada semana, 2s: cada dos semanas, m: cada mes"))
                .andMayThrowException(() -> args[2], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        Player jugadorAContratarPlayer = Bukkit.getPlayer(args[1]);
        double sueldo = Double.parseDouble(args[3]);
        String cargo;
        if(args.length == 5){
            cargo = args[4];
        }else{
            cargo = "Trabajador";
        }

        TipoSueldo tipoSueldo = TipoSueldo.ofCodigo(args[4]);

        ContratarSolicitud solicitud = new ContratarSolicitud(player.getName(), jugadorAContratarPlayer.getName(), args[2], sueldo, tipoSueldo, cargo);
        solicitud.enviarSolicitud();

        MySQL.desconectar();
    }
}
