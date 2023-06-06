package es.serversurvival.v2.minecraftserver.empresas.misempleos;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("empresas misempleos")
@RequiredArgsConstructor
public final class MisEmpleosCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, MisEmpleosMenu.class);
    }
}
