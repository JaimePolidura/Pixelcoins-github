package es.serversurvival.deudas.prestar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
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

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "deudas prestar",
        isSubCommand = true,
        args = {"jugador", "pixelcoins", "dias", "[interes]¡1!"}
)
public class PrestarComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<PrestarComando> {
    @Override
    public void execute(PrestarComando comando, CommandSender player) {
        String jugador = comando.getJugador();
        int pixelcoins = comando.getPixelcoins();
        int dias = comando.getDias();
        int interes = comando.getInteres();
        double pixelcoinsConIntereses = Funciones.aumentarPorcentaje(pixelcoins, interes);

        ValidationResult result = ValidatorService
                .startValidating(jugador, JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No puedes ser tu mimsmo"))
                .and(pixelcoins, NaturalNumber)
                .and(dias, NaturalNumber)
                .and(interes, NaturalNumber)
                .and(dias >= pixelcoins, True.of("Los dias no pueden ser superior a las pixelcoins"))
                .and(MenuManager.getByPlayer(jugador) != null, False.of("Ya le han enviado una solicitud"))
                .and(pixelcoinsConIntereses, SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PrestamoSolicitud solicitud = new PrestamoSolicitud(
                player.getName(),
                jugador,
                pixelcoins,
                dias,
                interes
        );

    }
}
