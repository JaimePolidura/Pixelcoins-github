package es.serversurvival.minecraftserver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import org.bukkit.entity.Player;

@Command(
        value = "cls",
        explanation = "Limpiar el chat"
)
public final class ClearChatCommandRunner implements CommandRunnerNonArgs {
    @Override
    public void execute(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage("");
        }
    }
}
