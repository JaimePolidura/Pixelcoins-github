package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.util.Funciones;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.security.acl.Owner;

import static es.serversurvival.validaciones.Validaciones.*;

public class EditarDescEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "editardescripccion";
        private final String sintaxis = "/empresas editardescripccion <empresa> <nueva desc>";
    private final String ayuda = "Editar la descripcion de tu empresa";

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
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length >= 3, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), mensajeUsoIncorrecto(), MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripccino no puede ser tan larga"))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarDescripciom(args[1], Funciones.buildStringFromArray(args, 2), player);
        MySQL.desconectar();
    }
}
