package es.serversurvival.nfs.empresas.depositar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas depositar")
public class DepositarPixelcoinsComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas depositar <empresa> <pixelcoins>";
    private final DepositarPixelcoinsUseCase useCasse = DepositarPixelcoinsUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.PositiveNumber, Validaciones.SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        double pixelcoins = Double.parseDouble(args[2]);
        String nombreEmpresa = args[1];

        useCasse.depositar(nombreEmpresa, player.getName(), pixelcoins);

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(pixelcoins) + " PC" + GOLD
                + " en tu empresa: " + DARK_AQUA + nombreEmpresa, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
