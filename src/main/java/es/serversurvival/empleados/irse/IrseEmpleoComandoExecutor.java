package es.serversurvival.empleados.irse;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Supplier;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command("empleos irse")
public class IrseEmpleoComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<IrseEmpleoComando> {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto: /empleos irse <empresa>";
    private final IrseEmpresaUseCase useCase = IrseEmpresaUseCase.INSTANCE;

    @Override
    public void execute(IrseEmpleoComando command, CommandSender player) {
        String empresa = command.getEmpresa();

        ValidationResult result = ValidatorService
                .startValidating(empresasMySQL.getEmpresa(empresa) != null, True.of("Esa empresa no exsiste"))
                .and(trabajaEnLaEmpresa(empresa, player.getName()), True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        useCase.irse(player.getName(), empresa);

        player.sendMessage(ChatColor.GOLD + "Te has ido de: " + empresa);
    }

    private boolean trabajaEnLaEmpresa (String empresa, String jugador) {
        return empleadosMySQL.trabajaEmpresa(jugador, empresa);
    }
}
