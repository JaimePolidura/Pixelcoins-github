package es.serversurvival.v2.minecraftserver.deudas.emitir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(
        value = "deudas emitir",
        explanation = "Poner el en mercado (/deudas mercado) una deuda. Cuando alguien la compre te habra prestado las " +
                "pixelcoins y empezaras a pagarles las cuotas"
)
public final class EmitirDeudaCommandRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(Player sender) {

    }
}
