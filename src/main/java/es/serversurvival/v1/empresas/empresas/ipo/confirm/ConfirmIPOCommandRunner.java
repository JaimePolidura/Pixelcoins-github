package es.serversurvival.v1.empresas.empresas.ipo.confirm;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.empresas.empresas.ipo.IPOCommand;
import es.serversurvival.v1.empresas.empresas.ipo.RealizarIPOUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@Command(
        value = "empresas confirmipo",
        args = {"empresa", "accionesTotales", "accionesOwner", "precioPorAccion"},
        explanation = "Puedes sacar tus empresas a la 'bolsa' donde el resto de jugadores podran comprar las cantidad. " +
        "Por cada venta de la empresa a jugadores tu empresa recaudara las pixelcoins. Para mas ayuda pregunta al admin"
)
@AllArgsConstructor
public final class ConfirmIPOCommandRunner implements CommandRunnerArgs<IPOCommand> {
    private final RealizarIPOUseCase iposUseCase;

    @Override
    public void execute(IPOCommand command, CommandSender sender) {
        this.iposUseCase.makeIPO(sender.getName(), command);
    }
}
