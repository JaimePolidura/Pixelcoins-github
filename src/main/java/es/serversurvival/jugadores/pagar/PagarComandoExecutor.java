package es.serversurvival.jugadores.pagar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static main.ValidatorService.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "pagar",
        args = {"pagado", "pixelcoins"}
)
public class PagarComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<PagarComando> {
    private final PagarUseCase pagarUseCase;
    private final String usoIncorrecto = DARK_RED + "/pagar <jugador> <pixelcoins>";

    public PagarComandoExecutor() {
        this.pagarUseCase = new PagarUseCase();
    }

    @Override
    public void execute(PagarComando pagarComando, CommandSender sender) {
        String pagador = sender.getName();
        double pixelcoins = pagarComando.getPixelcoins();
        String pagado = pagarComando.getPagado();

        ValidationResult result = startValidating(pixelcoins, PositiveNumber, SuficientesPixelcoins.of(pagador, pixelcoins))
                .and(pagado , JugadorRegistrado, NotEqualsIgnoreCase.of(pagador))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        this.pagarUseCase.realizarPagoManual(pagador, pagado, pixelcoins);

        sendMessage((Player) sender, pagado, pixelcoins);
    }

    private void sendMessage (Player pagador, String pagado, double pixelcoins) {
        pagador.sendMessage(GOLD + "Has pagado: " + GREEN + AllMySQLTablesInstances.formatea.format(pixelcoins) + " PC " + GOLD + "a " + pagado);
        String mensajeSiEstaOnline = GOLD + pagador.getName() + " te ha pagado: " + GREEN + "+" + AllMySQLTablesInstances.formatea.format(pixelcoins) + " PC " + AQUA + "(/estadisticas)";
        Funciones.enviarMensaje(pagado, mensajeSiEstaOnline, mensajeSiEstaOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        String mensajeOnline = GOLD + pagador.getName() + " te ha pagado " + GREEN + formatea.format(pixelcoins) + "PC!";
        String mensajeOffline = pagador.getName() + " te ha pagado " + formatea.format(pixelcoins) + "PC!";

        Funciones.enviarMensaje(pagado, mensajeOnline, mensajeOffline);
    }
}
