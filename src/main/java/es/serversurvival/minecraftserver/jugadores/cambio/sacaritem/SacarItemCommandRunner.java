package es.serversurvival.minecraftserver.jugadores.cambio.sacaritem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacar",
        explanation = "Sacar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class SacarItemCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player commandSender) {
        this.menuService.open(commandSender, SacarItemMenu.class);
    }
}
