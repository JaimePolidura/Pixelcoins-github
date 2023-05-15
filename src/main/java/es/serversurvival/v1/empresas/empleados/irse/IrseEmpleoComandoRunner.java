package es.serversurvival.v1.empresas.empleados.irse;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "empleos irse",
        args = {"empresa"},
        explanation = "Irse de una empresa en la que estes contratado <empresa> nombre de la empresa para irse"
)
@AllArgsConstructor
public class IrseEmpleoComandoRunner implements CommandRunnerArgs<IrseEmpleoComando> {
    private final IrseEmpresaUseCase useCase;

    @Override
    public void execute(IrseEmpleoComando command, CommandSender player) {
        useCase.irse(player.getName(), command.getEmpresa());

        player.sendMessage(ChatColor.GOLD + "Te has ido de: " + command.getEmpresa());
    }
}
