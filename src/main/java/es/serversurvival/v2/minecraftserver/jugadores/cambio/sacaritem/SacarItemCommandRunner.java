package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacaritem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacar",
        explanation = "Sacar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class SacarItemCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender commandSender) {
        this.menuService.open((Player) commandSender, SacarItemMenu.class);
    }
}
