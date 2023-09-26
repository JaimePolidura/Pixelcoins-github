package es.serversurvival.minecraftserver.jugadores.fly;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import org.bukkit.entity.Player;

@Command(value = "fly", args = {"player"}, needsOp = true)
public final class FlyCommandRunner implements CommandRunnerArgs<FlyCommand> {
    @Override
    public void execute(FlyCommand flyCommand, Player player) {
        flyCommand.getPlayer().setFlying(flyCommand.getPlayer().isFlying());
    }
}
