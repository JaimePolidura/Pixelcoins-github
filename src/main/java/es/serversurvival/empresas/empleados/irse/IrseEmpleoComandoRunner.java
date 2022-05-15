package es.serversurvival.empresas.empleados.irse;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(
        value = "empleos irse",
        args = {"empresa"},
        explanation = "Irse de una empresa en la que estes contratado <empresa> nombre de la empresa para irse"
)
public class IrseEmpleoComandoRunner implements CommandRunnerArgs<IrseEmpleoComando> {
    private final IrseEmpresaUseCase useCase;

    public IrseEmpleoComandoRunner() {
        this.useCase = new IrseEmpresaUseCase();
    }

    @Override
    public void execute(IrseEmpleoComando command, CommandSender player) {
        useCase.irse(player.getName(), command.getEmpresa());

        player.sendMessage(ChatColor.GOLD + "Te has ido de: " + command.getEmpresa());
    }
}
