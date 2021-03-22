package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.menus.menus.solicitudes.VenderJugadorSolicitud;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;


@Command(name = "venderjugador")
public class VenderJugador extends ComandoUtilidades implements CommandRunner {
    private final String mensajeUsoIncorrecto = DARK_RED + "/venderjugador <jugador> <precio>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        MySQL.conectar();
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2))
                .andMayThrowException(() -> args[0], mensajeUsoIncorrecto, NotEqualsIgnoreCase.of(player.getName()), JugadorOnline, InventarioNoLleno)
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto ,PositiveNumber, SuficientesPixelcoins.of(() -> args[0], "El jugador no tiene las suficintes pixelcoins"))
                .and(player.getInventory().getItemInMainHand(), NotNull)
                .and(player.getInventory().getItemInMainHand().getType().toString(), NotEqualsIgnoreCase.of("AIR", "No puede ser aire"))
                .validateAll();

        MySQL.desconectar();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        VenderJugadorSolicitud solicitud = new VenderJugadorSolicitud(player, Bukkit.getPlayer(args[0]), player.getInventory().getItemInMainHand(), Double.parseDouble(args[1]));
        solicitud.enviarSolicitud();
    }
}
