package es.serversurvival.v2.minecraftserver.deudas.prestar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "deudas prestar",
        explanation = "Prestar pixelcoins a un jugador"
)
public final class PrestarCommandRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(Player commandSender) {

    }
}
