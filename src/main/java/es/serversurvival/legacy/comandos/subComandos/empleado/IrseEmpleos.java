package es.serversurvival.legacy.comandos.subComandos.empleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

@Command(name = "empleos irse")
public class IrseEmpleos extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto: /empleos irse <empresa>";

    @Override
    public void execute(CommandSender jugadorPlayer, String[] args) {

        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> empresasMySQL.getEmpresa(args[1]) != null, usoIncorrecto, Validaciones.True.of("Esa empresa no exsiste"))
                .and(trabajaEnLaEmpresa(() -> args[1], jugadorPlayer.getName()), Validaciones.True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();

        if(result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        empleadosMySQL.irseEmpresa(args[1], (Player) jugadorPlayer);
    }

    private boolean trabajaEnLaEmpresa (Supplier<String> empresaSupplier, String jugador) {
        try{
            String empresaNombre = empresaSupplier.get();

            return empleadosMySQL.trabajaEmpresa(jugador, empresaNombre);
        }catch (Exception e) {
            return false;
        }
    }
}
