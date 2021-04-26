package es.serversurvival.nfs.jugadores.pagar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import static es.serversurvival.nfs.utils.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command("pagar")
public class PagarComando extends PixelcoinCommand implements CommandRunner {
    private final PagarUseCase pagarUseCase;
    private final String usoIncorrecto = DARK_RED + "/pagar <jugador> <pixelcoins>";

    public PagarComando() {
        this.pagarUseCase = new PagarUseCase();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String pagador = sender.getName();

        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(2, "Same"))
                .andMayThrowException(() -> args[1], "Introduce un numero no texto", Validaciones.PositiveNumber, Validaciones.SuficientesPixelcoins.of(pagador, () -> args[1]))
                .andMayThrowException(() -> args[0],usoIncorrecto , Validaciones.JugadorRegistrado, Validaciones.NotEqualsIgnoreCase.of(pagador))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        double pixelcoins = Double.parseDouble(args[1]);
        String pagado = args[1];

        this.pagarUseCase.realizarPagoManual(sender.getName(), args[0], Double.parseDouble(args[1]));

        sender.sendMessage(GOLD + "Has pagado: " + GREEN + formatea.format(pixelcoins) + " PC " + GOLD + "a " + pagado);
        String mensajeSiEstaOnline = GOLD + pagador + " te ha pagado: " + GREEN + "+" + formatea.format(pixelcoins) + " PC " + AQUA + "(/estadisticas)";
        enviarMensaje(pagado, mensajeSiEstaOnline, mensajeSiEstaOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }
}
