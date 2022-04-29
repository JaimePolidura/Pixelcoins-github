package es.serversurvival.empresas.depositar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas depositar",
        args = {"empresa", "pixelcoins"},
        explanation = "Depositar pixelcoins en la empresa <empresa>"
)
public class DepositarPixelcoinsComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<DepositarPixelcoinsComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas depositar <empresa> <pixelcoins>";
    private final DepositarPixelcoinsUseCase useCasse = DepositarPixelcoinsUseCase.INSTANCE;

    @Override
    public void execute(DepositarPixelcoinsComando depositarPixelcoinsComando, CommandSender player) {
        String empresa = depositarPixelcoinsComando.getEmpresa();
        double pixelcoins = depositarPixelcoinsComando.getPixelcoins();

        ValidationResult result = ValidatorService
                .startValidating(empresa, OwnerDeEmpresa.of(player.getName()))
                .and(pixelcoins, PositiveNumber, SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        useCasse.depositar(empresa, player.getName(), pixelcoins);

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(pixelcoins) + " PC" + GOLD
                + " en tu empresa: " + DARK_AQUA + empresa, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
