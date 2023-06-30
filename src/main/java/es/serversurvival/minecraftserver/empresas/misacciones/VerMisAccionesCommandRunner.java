package es.serversurvival.minecraftserver.empresas.misacciones;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("empresas misacciones")
@RequiredArgsConstructor
public final class VerMisAccionesCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, MisEmpresasAccionesMenu.class);
    }
}
