package es.serversurvival.deudas.pagarTodo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "deudas pagar",
        args = {"id"},
        explanation = "Pagar toda la deuda que tengas con un jugador <id> id de la deuda se ve en /deudas ver"
)
public class PagarDeudaCompletaComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<PagarDeudaCompletaComando> {
    private final PagarDeudaCompletaUseCase usecase = PagarDeudaCompletaUseCase.INSTANCE;
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas pagar <id>";

    @Override
    public void execute(PagarDeudaCompletaComando pagarDeudaCompletaComando, CommandSender player) {
        int id = pagarDeudaCompletaComando.getId();
        double pixelcoinsRestantes = deudasMySQL.getDeuda(id).getPixelcoins_restantes();

        ValidationResult result = ValidatorService
                .startValidating(esDeudor(id, player.getName()), True.of("No eres deudor de esa deuda"))
                .and(pixelcoinsRestantes, SuficientesPixelcoins.of(player.getName(), "No tienes las suficientes pixelcoins"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        Deuda deudaPagada = usecase.pagarDeuda(id);

        Funciones.enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Has pagado a " + deudaPagada.getAcredor() + " toda la deuda: "
                + ChatColor.GREEN + formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + player.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN +
                formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC";

        String mensajeOffline = player.getName() + " ta ha pagado toda la deuda: " + deudaPagada.getPixelcoins_restantes() + " PC";
        Funciones.enviarMensaje(deudaPagada.getAcredor(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

    }

    private boolean esDeudor (int idSupplier, String deudor) {
        Try<Boolean> deudaTry = Try.of(() -> deudasMySQL.esDeudorDeDeuda(idSupplier, deudor));

        return deudaTry.isSuccess() && deudaTry.get();
    }
}
