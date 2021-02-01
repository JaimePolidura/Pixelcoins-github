package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.menus.menus.confirmaciones.BorrrarEmpresaConfirmacion;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class BorrarEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "borrar";
    private final String sintaxis = "/empresa borrar <empresa>";
    private final String ayuda = "Borrar tu propia empresa";

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

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        BorrrarEmpresaConfirmacion confirmacionMenu = new BorrrarEmpresaConfirmacion(player, args[1]);
    }
}
