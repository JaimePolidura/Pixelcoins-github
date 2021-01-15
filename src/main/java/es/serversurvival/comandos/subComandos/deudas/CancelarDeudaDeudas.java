package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class CancelarDeudaDeudas extends DeudasSubCommand {
    private final String scnombre = "cancelar";
    private final String sintaxis = "/deudas cancelar <id>";
    private final String ayuda = "cacelar toda la deuda a un jugador, la id se ve en /deudas ver";

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

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), NaturalNumber)
                .andMayThrowException(() -> existeDeuda(args), mensajeUsoIncorrecto(), True.of("No hay ninguna deuda con ese id"))
                .andMayThrowException(() -> acredorDeDeuda(args, player), mensajeUsoIncorrecto(), True.of("No eres el acredor de esa deuda"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        deudasMySQL.cancelarDeuda(player, Integer.parseInt(args[1]));
        MySQL.desconectar();
    }

    private boolean existeDeuda (String[] args) {
        try{
            return deudasMySQL.getDeuda(Integer.parseInt(args[1])) != null;
        }catch (Exception e) {
            return false;
        }
    }

    private boolean acredorDeDeuda (String[] args, Player player) {
        try{
            return deudasMySQL.getDeuda(Integer.parseInt(args[1])).getAcredor().equalsIgnoreCase(player.getName());
        }catch (Exception e) {
            return false;
        }
    }

}
