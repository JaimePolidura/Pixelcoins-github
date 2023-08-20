package es.serversurvival.minecraftserver.config;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunner;

@Command(
        value = "config ayuda",
        needsOp = true,
        isHelper = true
)
public final class ConfigHelperCommandRunner implements CommandRunner {
}
