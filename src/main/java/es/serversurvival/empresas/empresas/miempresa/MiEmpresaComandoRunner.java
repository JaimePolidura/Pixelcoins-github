package es.serversurvival.empresas.empresas.miempresa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas miempresa",
        args = {"empresa"},
        explanation = "Ver todos los datos de tu <empresa>"
)
public class MiEmpresaComandoRunner implements CommandRunnerArgs<MiEmpresaComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas miempresa <empresa>";

    @Override
    public void execute(MiEmpresaComando miEmpresaComando, CommandSender sender) {
        String empresa = miEmpresaComando.getEmpresa();

        ValidationResult result = ValidatorService
                .startValidating(empresa, OwnerDeEmpresa.of(sender.getName()))
                .validateAll();

        if(result.isFailed()) {
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        EmpresasVerMenu menu = new EmpresasVerMenu((Player) sender, empresa);
    }
}
