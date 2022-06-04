package es.serversurvival.jugadores.pagar;

import es.jaime.EventBus;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "pagar",
        args = {"destino", "pixelcoins"},
        explanation = "Pagar a un jugador online con tus pixelcoins"
)
public class PagarComandoRunner implements CommandRunnerArgs<PagarComando> {
    private final PagarUseCase pagarUseCase;

    public PagarComandoRunner() {
        this.pagarUseCase = new PagarUseCase(
                DependecyContainer.get(JugadoresService.class),
                DependecyContainer.get(EventBus.class)
        );
    }

    @Override
    public void execute(PagarComando comando, CommandSender sender) {
        String pagador = sender.getName();
        double pixelcoins = comando.getPixelcoins();

        this.pagarUseCase.realizarPago(pagador, comando.getDestino().getName(), pixelcoins);

        sendMessage((Player) sender, comando.getDestino().getName(), pixelcoins);
    }

    private void sendMessage (Player pagador, String pagado, double pixelcoins) {
        pagador.sendMessage(GOLD + "Has pagado: " + GREEN + FORMATEA.format(pixelcoins) + " PC " + GOLD + "a " + pagado);

        String mensajeOnline = GOLD + pagador.getName() + " te ha pagado " + GREEN + FORMATEA.format(pixelcoins) + "PC!";
        String mensajeOffline = pagador.getName() + " te ha pagado " + FORMATEA.format(pixelcoins) + "PC!";

        Funciones.enviarMensaje(pagado, mensajeOnline, mensajeOffline);
    }
}
