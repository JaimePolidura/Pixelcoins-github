package es.serversurvival.empresas.despedir;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import es.serversurvival.empleados.despedir.DespedirEmpleadoUseCase;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas despedir",
        isSubCommand = true,
        args = {"empresa", "jugador", "razon"}
)
public class DespedirEmpleadoComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<DespedirEmpleadoComando> {
    private final DespedirEmpleadoUseCase useCase = DespedirEmpleadoUseCase.INSTANCE;

    @Override
    public void execute(DespedirEmpleadoComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugador = comando.getJugador();
        String razon = comando.getRazon();

        ValidationResult result = ValidatorService
                .startValidating(jugador, TrabajaEmpresa.en(empresa), NotEqualsIgnoreCase.of(player.getName(), "No te puedes despedir a ti mismo"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        useCase.despedir(jugador, empresa, razon);

        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + jugador);
    }
}
