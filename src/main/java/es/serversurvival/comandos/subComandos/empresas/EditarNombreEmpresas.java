package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class EditarNombreEmpresas extends EmpresasSubCommand {
    private final String SCNOmbre = "editarnombre";
    private final String sintaxis = "/empresas editarnombre <empresa> <nuevo nombre>";
    private final String ayuda = "cambiar el nombre de una empresa tuya";

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

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()), MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan largo"))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NombreEmpresaNoPillado)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarNombre(player, args[1], args[2]);
        MySQL.desconectar();
    }
}
