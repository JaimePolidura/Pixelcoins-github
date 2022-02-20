package es.serversurvival.empresas.borrar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas borrar",
        args = {"empresa"},
        isSubCommand = true
)
public class BorrarEmpresaComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<BorrarEmpresaComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresa borrar <empresa>";

    @Override
    public void execute(BorrarEmpresaComando borrarEmpresaComando, CommandSender player) {
        String empresa = borrarEmpresaComando.getEmpresa();

        ValidationResult result = ValidatorService
                .startValidating(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        BorrrarEmpresaConfirmacion confirmacionMenu = new BorrrarEmpresaConfirmacion((Player) player, empresa);
    }
}
