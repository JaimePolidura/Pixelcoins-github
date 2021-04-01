package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static main.ValidationsService.*;

@Command(name = "comprar")
public class Comprar extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto /comprar <empresa> <precio>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        ValidationResult result = startValidating(args, NotNull.message(ChatColor.DARK_RED + "Uso incorrecto /comprar"))
                .and(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, PositiveNumber, SuficientesPixelcoins.of(sender.getName()))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        MySQL.conectar();
        transaccionesMySQL.comprarServivio(args[0], Double.parseDouble(args[1]), (Player) sender);
        MySQL.desconectar();
    }
}
