package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.menus.solicitudes.VenderSolicitud;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class VenderEmpresas extends EmpresasSubCommand {
    private final String SCNOmbre = "vender";
    private final String sintaxis = "/empresas vender <empresa> <jugador> <precio>";
    private final String ayuda = "vender una empresa tuya a un determinado precio a un jugador";

    public String getSCNombre() {
        return SCNOmbre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 4, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .andMayThrowException(() -> args[3], mensajeUsoIncorrecto(), PositiveNumber, SuficientesPixelcoins.of(() -> args[2], "No tiene tantas pixelcoins como crees xdd"))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        VenderSolicitud venderSolicitud = new VenderSolicitud(player.getName(), args[2], args[1], Double.parseDouble(args[3]));
        venderSolicitud.enviarSolicitud();

        MySQL.desconectar();
    }
}
