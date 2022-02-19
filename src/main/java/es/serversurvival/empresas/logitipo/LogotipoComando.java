package es.serversurvival.empresas.logitipo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas logotipo")
public class LogotipoComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas logotipo <empresa>";
    private final CambiarLogitpoUseCase useCase = CambiarLogitpoUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        String logitpo = player.getInventory().getItemInMainHand().getType().toString();

        ValidationResult result = ValidatorService.startValidating(args.length == 2, Validaciones.True.of(usoIncorrecto))
                .and(logitpo, Validaciones.NotEqualsIgnoreCase.of("AIR", "Tienes que tener un item en la mano"))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if (result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        this.useCase.cambiar(args[1], logitpo);

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + logitpo, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
