package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.menus.menus.EmpresasVerMenu;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class MiEmpresaEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "miempresa";
    private final String sintaxis = "/empresas miempresa <empresa>";
    private final String ayuda = "ver los empleados, pixelcoins etc de mi propia empresa";

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

        ValidationResult result = ValidationsService.startValidating(args.length == 2, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        EmpresasVerMenu menu = new EmpresasVerMenu(player, args[1]);
        menu.openMenu();
    }
}
