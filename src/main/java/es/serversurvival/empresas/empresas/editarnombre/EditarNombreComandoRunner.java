package es.serversurvival.empresas.empresas.editarnombre;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas editarnombre",
        args = {"empresa", "nuevoNombre"},
        explanation = "Cambiar el nombre de tu empresa a otro, el nombre no puede estar cogido"
)
public class EditarNombreComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<EditarNombreComando> {
    private final EditarNombreUseCase useCase;

    public EditarNombreComandoRunner() {
        this.useCase = new EditarNombreUseCase();
    }

    @Override
    public void execute(EditarNombreComando editarNombreComando, CommandSender sender) {
        useCase.editar(editarNombreComando.getEmpresa(), editarNombreComando.getNuevoNombre(), sender.getName());

        Funciones.enviarMensajeYSonido((Player) sender, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
