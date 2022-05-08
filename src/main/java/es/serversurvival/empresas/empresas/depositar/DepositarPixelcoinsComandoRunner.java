package es.serversurvival.empresas.empresas.depositar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas depositar",
        args = {"empresa", "pixelcoins"},
        explanation = "Depositar pixelcoins en la empresa <empresa>"
)
public class DepositarPixelcoinsComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<DepositarPixelcoinsComando> {
    private final DepositarPixelcoinsUseCase useCasse;

    public DepositarPixelcoinsComandoRunner(){
        this.useCasse = new DepositarPixelcoinsUseCase();
    }

    @Override
    public void execute(DepositarPixelcoinsComando comando, CommandSender player) {
        useCasse.depositar(comando.getEmpresa(), player.getName(), comando.getPixelcoins());

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(comando.getPixelcoins()) + " PC" + GOLD
                + " en tu empresa: " + DARK_AQUA + comando.getEmpresa(), Sound.ENTITY_PLAYER_LEVELUP);
    }
}
