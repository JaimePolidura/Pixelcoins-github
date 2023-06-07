package es.serversurvival.minecraftserver.jugadores.pagar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.jugadores.pagar.HacerPagarParametros;
import es.serversurvival.pixelcoins.jugadores.pagar.PagarUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Command(
        value = "pagar",
        args = {"destino", "pixelcoins"},
        explanation = "Pagar a un nombreAccionista online con tus pixelcoins"
)
@AllArgsConstructor
public class PagarComandoRunner implements CommandRunnerArgs<PagarComando> {
    private final PagarUseCase pagarUseCase;

    @Override
    public void execute(PagarComando comando, Player sender) {
        double pixelcoins = comando.getPixelcoins();

        pagarUseCase.hacerPago(HacerPagarParametros.of(sender.getUniqueId(), comando.getDestino().getUniqueId(), comando.getPixelcoins()));

        sendMessage(sender, comando.getDestino(), pixelcoins);
    }

    private void sendMessage (Player pagador, Player pagado, double pixelcoins) {
        pagador.sendMessage(GOLD + "Has pagado: " + GREEN + Funciones.FORMATEA.format(pixelcoins) + " PC " + GOLD + "a " + pagado);

        MinecraftUtils.enviarMensajeYSonido(pagado.getUniqueId(),GOLD + pagador.getName() + " te ha pagado " + GREEN + Funciones.FORMATEA.format(pixelcoins) + "PC!",
                Sound.ENTITY_PLAYER_LEVELUP);
    }
}
