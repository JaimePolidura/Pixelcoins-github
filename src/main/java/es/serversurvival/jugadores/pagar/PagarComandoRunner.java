package es.serversurvival.jugadores.pagar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
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
        args = {"destino", "pixelcoins"},
        explanation = "Pagar a un jugador con tus pixelcoins"
)
public class PagarComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<PagarComando> {
    private final PagarUseCase pagarUseCase;
    private final String usoIncorrecto = DARK_RED + "/pagar <jugador> <pixelcoins>";

    public PagarComandoRunner() {
        this.pagarUseCase = new PagarUseCase();
    }

    @Override
    public void execute(PagarComando comando, CommandSender sender) {
        String pagador = sender.getName();
        double pixelcoins = comando.getPixelcoins();

        ValidationResult result = startValidating(pixelcoins, PositiveNumber, SuficientesPixelcoins.of(pagador, pixelcoins))
                .and(comando.getDestino() , JugadorRegistrado, NotEqualsIgnoreCase.of(pagador))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        this.pagarUseCase.realizarPagoManual(pagador, comando.getDestino().getName(), pixelcoins);

        sendMessage((Player) sender, comando.getDestino().getName(), pixelcoins);
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
