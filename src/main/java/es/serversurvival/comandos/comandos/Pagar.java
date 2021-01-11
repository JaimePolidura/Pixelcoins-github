package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class Pagar extends Comando {
    private final String cnombre = "pagar";
    private final String sintaxis = "/pagar <jugador> <pixelcoins>";
    private final String ayuda = "Pagar a un jugador un numero de pixelcoins";

    public String getCNombre() {
        return cnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        String senderName = player.getName();

        MySQL.conectar();
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, "Same"))
                .andMayThrowException(() -> args[1], "Introduce un numero no texto", PositiveNumber, SuficientesPixelcoins.of(senderName, () -> args[1]))
                .andMayThrowException(() -> args[0], "uso incorrecto", JugadorRegistrado, NotEqualsIgnoreCase.of(senderName))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        transaccionesMySQL.realizarPagoManual(player.getName(), args[0], Double.parseDouble(args[1]), player, "", TipoTransaccion.JUGADOR_PAGO_MANUAL);
        MySQL.desconectar();
    }
}
