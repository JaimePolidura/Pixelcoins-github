package es.serversurvival.bolsa.other.verordenespremarket;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "bolsa ordenes",
        explanation = "Ver todas las ordenes de compra y venta de acciones pendientes a ejecutar cuando el mercado este cerrado"
)
public class OrdenesBolsa implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu((Player) sender);
    }
}
