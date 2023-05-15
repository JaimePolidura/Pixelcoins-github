package es.serversurvival.v1.empresas.empresas.editardescripccion;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "empresas editardescripccion",
        args = {"empresa", "...descripcion"},
        explanation = "Editar la descripccion de tu empresa"
)
@AllArgsConstructor
public class EditarDescComandoRunner implements CommandRunnerArgs<EditarDescComando> {
    private final EditarDescUseCase useCase;

    @Override
    public void execute(EditarDescComando comando, CommandSender sender) {
        useCase.edit(comando.getEmpresa(), comando.getDescripcion(), sender.getName());

        sender.sendMessage(ChatColor.GOLD + "Has cambiado la descripccion de tu empresa: " + ChatColor.DARK_AQUA + comando.getEmpresa() +
                ChatColor.GOLD + " a ver en " + ChatColor.AQUA + "/empresas vertodas");
    }
}
