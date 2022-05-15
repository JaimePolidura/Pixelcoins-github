package es.serversurvival.deudas.prestar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.menus.MenuManager;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "deudas prestar",
        args = {"jugador", "pixelcoins", "dias", "[interes]¡1!"},
        explanation = "Prestar dinero a un jugador durante dias. La deuda se pagara cada dia"
)
public class PrestarComandoRunner implements CommandRunnerArgs<PrestarComando> {
    @Override
    public void execute(PrestarComando comando, CommandSender player) {
        String jugador = comando.getJugador();
        int pixelcoins = comando.getPixelcoins();
        int dias = comando.getDias();
        int interes = comando.getInteres();

        ValidationResult result = ValidatorService.startValidating(jugador, JugadorOnline)
                .and(MenuManager.getByPlayer(jugador) != null, False.of("Ya le han enviado una solicitud"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        new PrestamoSolicitud(
                player.getName(),
                jugador,
                pixelcoins,
                dias,
                interes
        );

    }
}
