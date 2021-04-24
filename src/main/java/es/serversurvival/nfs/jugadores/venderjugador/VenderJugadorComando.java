package es.serversurvival.nfs.jugadores.venderjugador;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;


@Command("venderjugador")
public class VenderJugadorComando extends PixelcoinCommand implements CommandRunner {
    private final String mensajeUsoIncorrecto = DARK_RED + "/venderjugador <jugador> <precio>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(2))
                .andMayThrowException(() -> args[0], mensajeUsoIncorrecto, Validaciones.NotEqualsIgnoreCase.of(player.getName()), Validaciones.JugadorOnline, Validaciones.InventarioNoLleno)
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto , Validaciones.PositiveNumber, Validaciones.SuficientesPixelcoins.of(() -> args[0], "El jugador no tiene las suficintes pixelcoins"))
                .and(player.getInventory().getItemInMainHand(), Validaciones.NotNull)
                .and(player.getInventory().getItemInMainHand().getType().toString(), Validaciones.NotEqualsIgnoreCase.of("AIR", "No puede ser aire"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        VenderJugadorSolicitud solicitud = new VenderJugadorSolicitud(player, Bukkit.getPlayer(args[0]), player.getInventory().getItemInMainHand(), Double.parseDouble(args[1]));
        solicitud.enviarSolicitud();
    }
}
