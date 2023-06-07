package es.serversurvival.minecraftserver.bolsa.vervalores;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("bolsa valores")
@RequiredArgsConstructor
public final class VerValoresCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, ElegirTipoActivoDisponibleMenu.class);
    }
}
