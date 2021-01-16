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

public class DespedirEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "despedir";
    private final String sintaxis = "/empresas despedir <empresa> <jugador> <razon>";
    private final String ayuda = "despedir a un jugador de tu propia empresa";

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

        ValidationResult result = ValidationsService.startValidating(args.length == 4, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), TrabajaEmpresa.en(() -> args[1]), NotEqualsIgnoreCase.of(player.getName(), "No te puedes despedir a ti mismo"))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empleadosMySQL.despedir(args[1], args[2], args[3], player);
        MySQL.desconectar();
    }
}
