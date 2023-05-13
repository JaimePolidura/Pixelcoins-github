package es.serversurvival.empresas.empresas.depositar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas depositar",
        args = {"empresa", "pixelcoins"},
        explanation = "Depositar pixelcoins en la empresa <empresa>"
)
@AllArgsConstructor
public class DepositarPixelcoinsComandoRunner implements CommandRunnerArgs<DepositarPixelcoinsComando> {
    private final DepositarPixelcoinsUseCase useCasse;
    private final EnviadorMensajes enviadorMensajes;

    @Override
    public void execute(DepositarPixelcoinsComando comando, CommandSender player) {
        useCasse.depositar(comando.getEmpresa(), player.getName(), comando.getPixelcoins());

        enviadorMensajes.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + FORMATEA.format(comando.getPixelcoins()) + " PC" + GOLD
                + " en tu empresa: " + DARK_AQUA + comando.getEmpresa(), Sound.ENTITY_PLAYER_LEVELUP);
    }
}
