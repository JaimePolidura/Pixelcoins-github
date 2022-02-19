package es.serversurvival.deudas.prestar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidatorService;
import main.validators.booleans.False;
import main.validators.booleans.True;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.function.Supplier;

import static org.bukkit.ChatColor.*;

@Command("deudas prestar")
public class PrestarComando extends PixelcoinCommand implements CommandRunner {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /deudas prestar <jugador> <dinero> <dias> [interes]";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidatorService.startValidating(args.length == 4 || args.length == 5, True.of(mensajeIncorrecto))
                .andMayThrowException(() -> args[1], mensajeIncorrecto, Validaciones.JugadorOnline, Validaciones.NotEqualsIgnoreCase.of(player.getName(), "No puedes ser tu mimsmo"))
                .andMayThrowException(() -> args[2], mensajeIncorrecto, Validaciones.NaturalNumber)
                .andMayThrowException(() -> args[3], mensajeIncorrecto, Validaciones.NaturalNumber)
                .andMayThrowException(() -> args[4], mensajeIncorrecto, Validaciones.NaturalNumber)
                .andMayThrowException(() -> Integer.parseInt(args[2]) >= Integer.parseInt(args[3]), mensajeIncorrecto, True.of("Los dias no pueden ser superior a las pixelcoins"))
                .andMayThrowException(() -> MenuManager.getByPlayer(Bukkit.getPlayer(args[1]).getName()) != null, mensajeIncorrecto, False.of("Ya le han enviado una solicitud"))
                .andMayThrowException(pixelcoinsDeudaConIntereses(args), mensajeIncorrecto, Validaciones.SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int interes = 0;
        if(args.length == 5){
            interes = Integer.parseInt(args[4]);
        }

        PrestamoSolicitud solicitud = new PrestamoSolicitud(player.getName(), Bukkit.getPlayer(args[1]).getName(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), interes);
        solicitud.enviarSolicitud();
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
