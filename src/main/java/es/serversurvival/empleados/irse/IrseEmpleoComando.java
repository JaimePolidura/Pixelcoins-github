package es.serversurvival.empleados.irse;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.utils.validaciones.Validaciones;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Supplier;

@Command("empleos irse")
public class IrseEmpleoComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto: /empleos irse <empresa>";
    private final IrseEmpresaUseCase useCase = IrseEmpresaUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> empresasMySQL.getEmpresa(args[1]) != null, usoIncorrecto, Validaciones.True.of("Esa empresa no exsiste"))
                .and(trabajaEnLaEmpresa(() -> args[1], player.getName()), Validaciones.True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String empresa = args[1];

        useCase.irse(player.getName(), args[1]);

        player.sendMessage(ChatColor.GOLD + "Te has ido de: " + empresa);
    }

    private boolean trabajaEnLaEmpresa (Supplier<String> empresaSupplier, String jugador) {
        Try<Boolean> booleanTry = Try.of(() -> empleadosMySQL.trabajaEmpresa(jugador, empresaSupplier.get()));

        return booleanTry.isSuccess() && booleanTry.get();
    }
}
