package es.serversurvival.empresas.empresas.editardescripccion;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "empresas editardescripccion",
        args = {"empresa", "descripcion..."},
        explanation = "Editar la descripccion de tu empresa"
)
public class EditarDescComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<EditarDescComando> {
    private final EditarDescUseCase useCase;

    public EditarDescComandoRunner(){
        this.useCase = new EditarDescUseCase();
    }

    @Override
    public void execute(EditarDescComando comando, CommandSender sender) {
        useCase.edit(comando.getEmpresa(), comando.getDescripcion(), sender.getName());

        sender.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + comando.getEmpresa() +
                ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }
}
