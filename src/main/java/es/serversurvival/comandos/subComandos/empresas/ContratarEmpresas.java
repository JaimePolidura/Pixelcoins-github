package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
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
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class ContratarEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "contratar";
    private final String sintaxis = "/empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]";
    private final String ayuda = "Contratar a un juagdor a tu empresa: <sueldo> cantidad de pixel coins que le vas a pagar, <tipo> frequencia de pago de sueldo (/ayuda empresario), [cargo] es opcional";

    public String getSCNombre() {
        return SCNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 5 || args.length == 6, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[3], mensajeUsoIncorrecto(), NaturalNumber)
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), JugadorOnline, NoLeHanEnviadoSolicitud, NoTrabajaEmpresa.en(() -> args[2]), NotEqualsIgnoreCase.of(player.getName(), "No te puedes contratar a ti mismo"))
                .andMayThrowException(() -> Empleados.esUnTipoDeSueldo(args[4]), mensajeUsoIncorrecto(), True.of("El tipo de sueldo solo puede ser d: cdda dia, s: cada semana, 2s: cada dos semanas, m: cada mes"))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
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

        ContratarSolicitud solicitud = new ContratarSolicitud(player.getName(), jugadorAContratarPlayer.getName(), args[2], sueldo, args[4], cargo);
        solicitud.enviarSolicitud();

        MySQL.desconectar();
    }
}
