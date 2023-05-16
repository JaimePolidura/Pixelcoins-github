package es.serversurvival.v2.minecraftserver.jugadores.pagar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.jugadores.pagar.PagarUseCase;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Command(
        value = "pagar",
        args = {"destino", "pixelcoins"},
        explanation = "Pagar a un nombreAccionista online con tus pixelcoins"
)
@AllArgsConstructor
public class PagarComandoRunner implements CommandRunnerArgs<PagarComando> {
    private final EnviadorMensajes enviadorMensajes;
    private final PagarUseCase pagarUseCase;

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

        enviadorMensajes.enviarMensaje(pagado, mensajeOnline, mensajeOffline);
    }
}
