package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "pagar")
public class Pagar extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "/pagar <jugador> <pixelcoins>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        String senderName = sender.getName();

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, "Same"))
                .andMayThrowException(() -> args[1], "Introduce un numero no texto", PositiveNumber, SuficientesPixelcoins.of(senderName, () -> args[1]))
                .andMayThrowException(() -> args[0],usoIncorrecto , JugadorRegistrado, NotEqualsIgnoreCase.of(senderName))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        transaccionesMySQL.realizarPagoManual(sender.getName(), args[0], Double.parseDouble(args[1]), (Player) sender, "");
    }
}
