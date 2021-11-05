package es.serversurvival.empresas.despedir;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival.empleados.despedir.DespedirEmpleadoUseCase;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.DARK_RED;

@Command("empresas despedir")
public class DespedirEmpleadoComando extends PixelcoinCommand implements CommandRunner {
    private final DespedirEmpleadoUseCase useCase = DespedirEmpleadoUseCase.INSTANCE;

    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas despedir <empresa> <jugador> <razon>";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length == 4, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.TrabajaEmpresa.en(() -> args[1]), Validaciones.NotEqualsIgnoreCase.of(player.getName(), "No te puedes despedir a ti mismo"))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String empleado = args[2];
        String empresa = args[1];
        String razon = args[3];

        useCase.despedir(empleado, empresa, razon);

        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleado);
    }
}
