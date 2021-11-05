package es.serversurvival.deudas.cancelar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.shared.utils.validaciones.Validaciones;
import es.serversurvival.shared.utils.Funciones;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.shared.utils.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.*;

@Command("deudas cancelar")
public class CancelarDeudaComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas cancelar <id>";
    private final CancelarDeudaUseCase useCase = CancelarDeudaUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.NaturalNumber)
                .andMayThrowException(() -> existeDeuda(args), usoIncorrecto, Validaciones.True.of("No hay ninguna deuda con ese id"))
                .andMayThrowException(() -> acredorDeDeuda(args, (Player) player), usoIncorrecto, Validaciones.True.of("No eres el acredor de esa deuda"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        Deuda deudaCancelada = useCase.cancelarDeuda((Player) player, Integer.parseInt(args[1]));

        Funciones.enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Has cancelado la deuda a " + deudaCancelada.getDeudor() + "!", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + player.getName() + " te ha cancelado la deuda de " + ChatColor.GREEN +
                formatea.format(deudaCancelada.getPixelcoins_restantes()) + " PC";
        Funciones.enviarMensaje(deudaCancelada.getDeudor(), mensajeOnline, mensajeOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private boolean existeDeuda (String[] args) {
        Try<Boolean> deudaTry = Try.of(() -> deudasMySQL.getDeuda(Integer.parseInt(args[1])) != null);

        return deudaTry.isSuccess() && deudaTry.get();
    }

    private boolean acredorDeDeuda (String[] args, Player player) {
        Try<Boolean> deudaTry = Try.of(() -> deudasMySQL.getDeuda(Integer.parseInt(args[1])).getAcredor().equalsIgnoreCase(player.getName()));

        return deudaTry.isSuccess() && deudaTry.get();
    }
}
