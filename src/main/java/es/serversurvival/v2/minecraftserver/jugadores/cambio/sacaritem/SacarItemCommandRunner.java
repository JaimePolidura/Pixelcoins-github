package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacaritem;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cambio sacar",
        explanation = "Sacar un objeto por pixelcoins"
)
@AllArgsConstructor
public final class SacarItemCommandRunner implements CommandRunnerNonArgs {
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;

        this.menuService.open(player, SacarItemMenu.class, this.jugadoresService.getByNombre(commandSender.getName()));
    }
}
