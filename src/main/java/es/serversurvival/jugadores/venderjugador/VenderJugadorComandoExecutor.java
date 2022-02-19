package es.serversurvival.jugadores.venderjugador;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "venderjugador",
        args = {"comprador", "pixelcoins"}
)
public class VenderJugadorComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<VenderJugadorComando> {
    private final String mensajeUsoIncorrecto = DARK_RED + "/venderjugador <jugador> <precio>";

    @Override
    public void execute(VenderJugadorComando venderJugadorComando, CommandSender sender) {
        Player player = (Player) sender;
        String comprador = venderJugadorComando.getComprador();
        double pixelcoins = venderJugadorComando.getPixelcoins();

        ValidationResult result = ValidatorService
                .startValidating(comprador, NotEqualsIgnoreCase.of(player.getName()), JugadorOnline, InventarioNoLleno)
                .and(pixelcoins, PositiveNumber, SuficientesPixelcoins.of(comprador, pixelcoins, "El jugador no tiene las suficientes pixelcoins"))
                .and(player.getInventory().getItemInMainHand(), NotNull)
                .and(player.getInventory().getItemInMainHand().getType().toString(), NotEqualsIgnoreCase.of("AIR", "No puede ser aire"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        VenderJugadorSolicitud solicitud = new VenderJugadorSolicitud(
                player,
                Bukkit.getPlayer(comprador),
                player.getInventory().getItemInMainHand(),
                pixelcoins
        );
    }
}
