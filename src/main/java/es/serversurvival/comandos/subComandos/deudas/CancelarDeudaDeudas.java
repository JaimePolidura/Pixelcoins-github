package es.serversurvival.comandos.subComandos.deudas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "deudas cancelar")
public class CancelarDeudaDeudas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas cancelar <id>";

    @Override
    public void execute(CommandSender player, String[] args) {

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, NaturalNumber)
                .andMayThrowException(() -> existeDeuda(args), usoIncorrecto, True.of("No hay ninguna deuda con ese id"))
                .andMayThrowException(() -> acredorDeDeuda(args, (Player) player), usoIncorrecto, True.of("No eres el acredor de esa deuda"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        deudasMySQL.cancelarDeuda((Player) player, Integer.parseInt(args[1]));
    }

    private boolean existeDeuda (String[] args) {
        try{
            return deudasMySQL.getDeuda(Integer.parseInt(args[1])) != null;
        }catch (Exception e) {
            return false;
        }
    }

    private boolean acredorDeDeuda (String[] args, Player player) {
        try{
            return deudasMySQL.getDeuda(Integer.parseInt(args[1])).getAcredor().equalsIgnoreCase(player.getName());
        }catch (Exception e) {
            return false;
        }
    }

}
