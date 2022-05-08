package es.serversurvival.empresas.empresas.despedir;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival.empresas.empleados.despedir.DespedirEmpleadoUseCase;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command(
        value = "empresas despedir",
        args = {"empresa", "jugador", "razon"},
        explanation = "Despedir a un jugador de tu empresa"
)
public class DespedirEmpleadoComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<DespedirEmpleadoComando> {
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
