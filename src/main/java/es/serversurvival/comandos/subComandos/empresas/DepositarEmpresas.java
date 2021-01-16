package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class DepositarEmpresas extends EmpresasSubCommand {
    private final String scnombre = "depositar";
    private final String sintaxis = "/empresas depositar <empresa> <pixelcoins>";
    private final String ayuda = "Depositar pixelcoins en tu empresa: para poder pagar el salario de los trabajadores";

    public String getSCNombre() {
        return scnombre;
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
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), PositiveNumber, SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        transaccionesMySQL.depositarPixelcoinsEmpresa(player, Double.parseDouble(args[2]), args[1]);
        MySQL.desconectar();
    }
}
