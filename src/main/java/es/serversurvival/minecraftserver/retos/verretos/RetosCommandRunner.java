package es.serversurvival.minecraftserver.retos.verretos;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "retos",
        explanation = "Ver los retos del servidor"
)
@AllArgsConstructor
public final class RetosCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerRetosModulosMenu.class);
    }
}
