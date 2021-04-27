package es.serversurvival.nfs.deudas.pagar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.deudas.mysql.Deuda;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.nfs.utils.Funciones.enviarMensaje;
import static es.serversurvival.nfs.utils.Funciones.enviarMensajeYSonido;
import static es.serversurvival.nfs.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command("deudas pagar")
public class PagarDeudaComando extends PixelcoinCommand implements CommandRunner {
    private final PagarDeudaUseCase usecase = PagarDeudaUseCase.INSTANCE;
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas pagar <id>";

    @Override
    public void execute(CommandSender player, String[] args) {
        Supplier<String> supplierPixelcoins = () -> String.valueOf(getDeuda(() -> args[1]).getPixelcoins_restantes());

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, NaturalNumber)
                .and(esDeudor(() -> args[1], player.getName()), True.of("No eres deudor de esa deuda"))
                .andMayThrowException(supplierPixelcoins, usoIncorrecto, SuficientesPixelcoins.of(player.getName(), "No tienes las suficientes pixelcoins"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        Deuda deudaPagada = usecase.pagarDeuda(player.getName(), Integer.parseInt(args[1]));

        enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Has pagado a " + deudaPagada.getAcredor() + " toda la deuda: "
                + ChatColor.GREEN + formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        String mensajeOnline = ChatColor.GOLD + player.getName() + " ta ha pagado toda la deuda: " + ChatColor.GREEN +
                formatea.format(deudaPagada.getPixelcoins_restantes()) + " PC";

        String mensajeOffline = player.getName() + " ta ha pagado toda la deuda: " + deudaPagada.getPixelcoins_restantes() + " PC";
        enviarMensaje(deudaPagada.getAcredor(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private boolean esDeudor (Supplier<String> idSupplier, String deudor) {
        Try<Boolean> deudaTry = Try.of(() -> deudasMySQL.esDeudorDeDeuda(Integer.parseInt(idSupplier.get()), deudor));

        return deudaTry.isSuccess() && deudaTry.get();
    }

    private Deuda getDeuda (Supplier<? extends String> supplierId) {
        Try<Deuda> deudaTry = Try.of(() -> deudasMySQL.getDeuda(Integer.parseInt(supplierId.get())));

        return deudaTry.isSuccess() ?
                deudaTry.get() :
                null;
    }
}
