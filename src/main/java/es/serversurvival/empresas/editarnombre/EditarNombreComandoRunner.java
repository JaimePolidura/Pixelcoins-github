package es.serversurvival.empresas.editarnombre;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(
        value = "empresas editarnombre",
        args = {"empresa", "nuevoNombre"},
        explanation = "Cambiar el nombre de tu empresa a otro, el nombre no puede estar cogido"
)
public class EditarNombreComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<EditarNombreComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas editarnombre <empresa> <nuevo nombre>";
    private final EditarNombreUseCase useCase = EditarNombreUseCase.INSTANCE;

    @Override
    public void execute(EditarNombreComando editarNombreComando, CommandSender sender) {
        String empresa = editarNombreComando.getEmpresa();
        String nuevoNombre = editarNombreComando.getNuevoNombre();

        ValidationResult result = ValidatorService
                .startValidating(empresa, OwnerDeEmpresa.of(sender.getName()), MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan largo"))
                .and(nuevoNombre, NombreEmpresaNoPillado)
                .validateAll();

        if(result.isFailed()) {
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        useCase.editar(empresa, nuevoNombre);

        Funciones.enviarMensajeYSonido((Player) sender, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
