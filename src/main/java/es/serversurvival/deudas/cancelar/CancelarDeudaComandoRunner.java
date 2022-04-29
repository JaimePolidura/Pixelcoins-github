package es.serversurvival.deudas.cancelar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidatorService;
import main.validators.booleans.True;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "deudas cancelar",
        args = {"id"},
        explanation = "Cancelar una deuda que tenga un jugador contigo <id> id de la deuda /deudas ver"
)
public class CancelarDeudaComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<CancelarDeudaComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas cancelar <id>";
    private final CancelarDeudaUseCase useCase = CancelarDeudaUseCase.INSTANCE;

    @Override
    public void execute(CancelarDeudaComando comando, CommandSender player) {
        int id = comando.getId();

        ValidationResult result = ValidatorService
                .startValidating(id, Validaciones.NaturalNumber)
                .andMayThrowException(() -> existeDeuda(comando), usoIncorrecto, True.of("No hay ninguna deuda con ese id"))
                .andMayThrowException(() -> acredorDeDeuda(comando, player), usoIncorrecto, True.of("No eres el acredor de esa deuda"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        Deuda deudaCancelada = useCase.cancelarDeuda((Player) player, id);

        enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Has cancelado la deuda a " + deudaCancelada.getDeudor() + "!", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + player.getName() + " te ha cancelado la deuda de " + ChatColor.GREEN +
                formatea.format(deudaCancelada.getPixelcoins_restantes()) + " PC";
        enviarMensaje(deudaCancelada.getDeudor(), mensajeOnline, mensajeOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private boolean existeDeuda (CancelarDeudaComando args) {
        return deudasMySQL.getDeuda(args.getId()) != null;
    }

    private boolean acredorDeDeuda (CancelarDeudaComando args, CommandSender sender) {
        return deudasMySQL.getDeuda(args.getId()).getAcredor().equalsIgnoreCase(sender.getName());
    }
}
