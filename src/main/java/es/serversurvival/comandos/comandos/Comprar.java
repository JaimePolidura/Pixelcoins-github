package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static main.ValidationsService.*;

//Esta clase hice el test directamente en el servidor por eso no tiene su archivo en la carpeta test
public class Comprar extends Comando {
    private final String CNOmbre = "comprar";
    private final String sintaxis = "/comprar <empresa> <precio>";
    private final String ayuda = "comprar un servicio a una empresa por unas pixelcoisn";

    public String getCNombre() {
        return CNOmbre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        ValidationResult result = startValidating(args, NotNull.message(mensajeUsoIncorrecto()))
                .and(args.length, Same.as(2, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), PositiveNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        MySQL.conectar();
        transaccionesMySQL.comprarServivio(args[0], Double.parseDouble(args[1]), player);
        MySQL.desconectar();
    }
}
