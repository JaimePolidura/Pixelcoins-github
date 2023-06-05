package es.serversurvival.v2.minecraftserver.deudas.emitir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v2.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(
        value = "deudas emitir",
        explanation = "Poner el en mercado (/deudas mercado) una deuda. Cuando alguien la compre te habra prestado las " +
                "pixelcoins y empezaras a pagarles las cuotas"
)
@AllArgsConstructor
public final class EmitirDeudaCommandRunner implements CommandRunnerNonArgs {
    private final WebActionUrlGenerator webActionUrlGenerator;

    @Override
    public void execute(Player sender) {

    }
}
