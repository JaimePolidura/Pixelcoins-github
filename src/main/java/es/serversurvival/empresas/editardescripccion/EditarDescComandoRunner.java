package es.serversurvival.empresas.editardescripccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas editardescripccion",
        args = {"empresa", "descripcion..."},
        explanation = "Editar la descripccion de tu empresa"
)
public class EditarDescComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<EditarDescComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editardescripccion <empresa> <nueva desc>";
    private EditarDescUseCase useCase = EditarDescUseCase.INSTANCE;

    @Override
    public void execute(EditarDescComando comando, CommandSender sender) {
        String empresa = comando.getEmpresa();
        String descripcion = comando.getDescripcion();

        ValidationResult result = ValidatorService
                .startValidating(descripcion, MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripccino no puede ser tan larga"))
                .and(empresa, OwnerDeEmpresa.of(sender.getName()))
                .validateAll();

        if (result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        useCase.edit(empresa, descripcion);

        sender.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + empresa +
                ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }
}
