package es.serversurvival.empresas.empresas.ipo.confirm;


import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empresas.ipo.IPOCommand;
import es.serversurvival.empresas.empresas.ipo.RealizarIPOUseCase;
import org.bukkit.command.CommandSender;

@Command(
        value = "empresas confirmipo",
        args = {"empresa", "accionesTotales", "accionesOwner", "precioPorAccion"},
        explanation = "Puedes sacar tus empresas a la 'bolsa' donde el resto de jugadores podran comprar las cantidad. " +
        "Por cada venta de la empresa a jugadores tu empresa recaudara las pixelcoins. Para mas ayuda pregunta al admin"
)
public final class ConfirmIPOCommandRunner implements CommandRunnerArgs<IPOCommand> {
    private final RealizarIPOUseCase iposUseCase;

    public ConfirmIPOCommandRunner() {
        this.iposUseCase = new RealizarIPOUseCase();
    }

    @Override
    public void execute(IPOCommand command, CommandSender sender) {
        this.iposUseCase.makeIPO(sender.getName(), command);
    }
}
