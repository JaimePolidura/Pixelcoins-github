package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.menus.solicitudes.PrestamoSolicitud;
import main.ValidationResult;
import main.ValidationsService;
import main.validators.booleans.False;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

public class PrestarDeudas extends DeudasSubCommand {
    private final String scnombre = "prestar";
    private final String sintaxis = "/deudas prestar <jugador> <dinero> <dias> [interes]";
    private final String ayuda = "prestar una deuda en pixelcoins en un plazo de unos diads. /ayuda deudas";

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

        ValidationResult result = ValidationsService.startValidating(args.length == 4 || args.length == 5, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No puedes ser tu mimsmo"))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NaturalNumber)
                .andMayThrowException(() -> args[3], mensajeUsoIncorrecto(), NaturalNumber)
                .andIfExists(() -> args[4], NaturalNumber)
                .andMayThrowException(() -> Integer.parseInt(args[2]) >= Integer.parseInt(args[3]), mensajeUsoIncorrecto(), True.of("Los dias no pueden ser superior a las pixelcoins"))
                .andMayThrowException(() -> MenuManager.getByPlayer(Bukkit.getPlayer(args[1]).getName()) != null, mensajeUsoIncorrecto(), False.of("Ya le han enviado una solicitud"))
                .andMayThrowException(pixelcoinsDeudaConIntereses(args), mensajeUsoIncorrecto(), SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        int interes = 0;
        if(args.length == 5){
            interes = Integer.parseInt(args[4]);
        }

        PrestamoSolicitud solicitud = new PrestamoSolicitud(player.getName(), Bukkit.getPlayer(args[1]).getName(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), interes);
        solicitud.enviarSolicitud();
        MySQL.conectar();
    }

    public static Supplier<String> pixelcoinsDeudaConIntereses (String[] args) {
        try{
            int interes = 0;
            int dinero = Integer.parseInt(args[2]);

            if(args.length == 5){
                interes = Integer.parseInt(args[4]);
            }

            int finalInteres = interes;
            return () -> String.valueOf(Funciones.aumentarPorcentaje(dinero, finalInteres));
        }catch (Exception e) {
            return () -> "";
        }
    }
}
