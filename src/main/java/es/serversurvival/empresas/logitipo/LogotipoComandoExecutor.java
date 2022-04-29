package es.serversurvival.empresas.logitipo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas logotipo",
        args = {"empresa"},
        explanation = "Cambiar el logotipo de tu empresa. Para esto selecciona un item en la mano y ejecuta el comando"
)
public class LogotipoComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<LogotipoComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas logotipo <empresa>";
    private final CambiarLogitpoUseCase useCase = CambiarLogitpoUseCase.INSTANCE;

    @Override
    public void execute(LogotipoComando logotipoComando, CommandSender sender) {
        Player player = (Player) sender;
        String logitpo = player.getInventory().getItemInMainHand().getType().toString();
        String empresa = logotipoComando.getEmpresa();

        ValidationResult result = ValidatorService
                .startValidating(logitpo, NotEqualsIgnoreCase.of("AIR", "Tienes que tener un item en la mano"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if (result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        this.useCase.cambiar(empresa, logitpo);

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + logitpo, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
